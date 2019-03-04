package fun.hercules.order.order.platform.order.service;

import fun.hercules.order.order.common.constants.ConfigProperties;
import fun.hercules.order.order.common.errors.ErrorCode;
import fun.hercules.order.order.common.exceptions.ConflictException;
import fun.hercules.order.order.common.exceptions.NotFoundException;
import fun.hercules.order.order.platform.exports.BusinessOrder;
import fun.hercules.order.order.platform.order.dto.PaymentInfo;
import fun.hercules.order.order.platform.order.model.OrderBill;
import fun.hercules.order.order.platform.order.repository.OrderBillRepository;
import fun.hercules.order.order.platform.order.specifications.OrderBillSpecs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static fun.hercules.order.order.common.errors.ErrorCode.ORDER_BILL_NUMBER_EXCEED_ONE;
import static org.springframework.data.jpa.domain.Specifications.where;


@Slf4j
@Service
public class OrderBillService {

    public static final int SIZE = 10000;
    public static final int PAGE = 0;
    private final OrderBillRepository orderBillRepository;
    private final ZoneId defaultZone;
    private final OrderService orderService;

    @Autowired
    public OrderBillService(OrderBillRepository orderBillRepository, OrderService orderService) {
        this.orderBillRepository = orderBillRepository;
        this.orderService = orderService;
        this.defaultZone = ConfigProperties.getInstance().getTimeZone();
    }

    public void saveOrUpdateOrderBill(PaymentInfo paymentInfo) {
        Optional<OrderBill> orderBillFromDb = orderBillRepository.findByOrderId(paymentInfo.getOrderId());
        OrderBill orderBill;
        if (orderBillFromDb.isPresent() ) {
            orderBill = orderBillFromDb.get();
            orderBill.setPayMethod(paymentInfo.getPayMethod());
            orderBill.setPayChannel(paymentInfo.getPayChannel());
        } else {
            BusinessOrder order = orderService.getByOrderId(paymentInfo.getOrderId());
            orderBill = OrderBill.builder()
                    .orderId(order.getId())
                    .productCharge(paymentInfo.getPayAmount())
                    .serviceCharge(paymentInfo.getPayAmount())
                    .commissionCharge(BigDecimal.ZERO)
                    .orderType(order.getOrderType())
                    .vendor(order.getVendor())
                    .payMethod(paymentInfo.getPayMethod())
                    .payChannel(paymentInfo.getPayChannel())
                    .currency(Currency.getInstance(paymentInfo.getCurrencyCode()))
                    .build();
            orderBill.initDefaultStatus();
            orderBillRepository.save(orderBill);
        }
    }

    public Page<OrderBill> findPageableOrderBills(LocalDate fromDate, LocalDate toDate, List<String> orderTypes, Pageable pageable) {
        if (!Objects.isNull(fromDate) && !Objects.isNull(toDate)) {
            Instant from = fromDate.atStartOfDay().atZone(defaultZone).toInstant();
            Instant to = toDate.atTime(LocalTime.MAX).atZone(defaultZone).toInstant();
            return orderBillRepository.findByOrderTypeInAndCreatedAtBetween(orderTypes, from, to, pageable);
        } else {
            return orderBillRepository.findByOrderTypeIn(orderTypes, pageable);
        }
    }

    public OrderBill findOrderBillByOrderId(String orderId) {
        int count = orderBillRepository.countByOrderId(orderId);
        if (count > 1) {
            log.warn(String.format("Order %s has %s bill", orderId, count));
            throw new ConflictException(ORDER_BILL_NUMBER_EXCEED_ONE);
        } else {
            return orderBillRepository.findByOrderId(orderId).orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_BILL_NOT_FOUND));
        }
    }

    public List<OrderBill> findAll(LocalDate fromDate, LocalDate toDate, String orderId, List<String> orderTypes) {
        Specifications<OrderBill> spec = null;
        if (fromDate != null && toDate != null) {

            Instant fromDateStart = fromDate.atStartOfDay().atZone(defaultZone).toInstant();
            Instant toDateEnd = toDate.atTime(LocalTime.MAX).atZone(defaultZone).toInstant();
            Range<Instant> range = new Range<>(fromDateStart, toDateEnd);

            spec = where(OrderBillSpecs.createdAtBetween(range));
        }

        if (StringUtils.isNotEmpty(orderId)) {
            spec = spec == null
                    ? where(OrderBillSpecs.equalWithOrderId(orderId))
                    : spec.and(OrderBillSpecs.equalWithOrderId(orderId));
        }

        if (CollectionUtils.isNotEmpty(orderTypes)) {
            spec = spec == null
                    ? where(OrderBillSpecs.inOrderTypes(orderTypes))
                    : spec.and(OrderBillSpecs.inOrderTypes(orderTypes));
        }
        return orderBillRepository.findAll(spec, new PageRequest(PAGE, SIZE, new Sort(Sort.Direction.DESC, "createdAt")))
                .getContent();
    }

    public List<OrderBill> getByOrderIds(Collection<String> orderIds, List<String> orderTypes) {
        return orderBillRepository.findByOrderIdInAndOrderTypeIn(orderIds, orderTypes);
    }

    public void save(OrderBill orderBill) {
        orderBillRepository.save(orderBill);
    }

    public List<OrderBill> save(List<OrderBill> orderBills) {
        return orderBillRepository.saveAll(orderBills);
    }
}
