package fun.hercules.order.order.platform.order.service;

import fun.hercules.order.order.clients.CachedUserClient;
import fun.hercules.order.order.common.constants.ConfigProperties;
import fun.hercules.order.order.platform.order.model.OrderType;
import fun.hercules.order.order.platform.order.model.PaymentStatus;
import fun.hercules.order.order.platform.order.model.PaymentTransaction;
import fun.hercules.order.order.platform.order.repository.PaymentTransactionRepository;
import fun.hercules.order.order.platform.order.specifications.PaymentTransactionSpecs;
import fun.hercules.order.order.platform.user.CurrentUser;
import fun.hercules.order.order.platform.user.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specifications.where;

@Slf4j
@Service
public class PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final OrderTypeAccessHandler orderTypeAccessHandler;
    private final CurrentUser currentUser;
    private final ZoneId defaultZone;

    private final CachedUserClient userClient;

    public PaymentTransactionService(PaymentTransactionRepository paymentTransactionRepository,
                                     OrderTypeAccessHandler orderTypeAccessHandler, CurrentUser currentUser, CachedUserClient userClient) {
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.orderTypeAccessHandler = orderTypeAccessHandler;
        this.currentUser = currentUser;
        this.userClient = userClient;
        this.defaultZone = ConfigProperties.getInstance().getTimeZone();
    }

    @PreAuthorize("hasAnyAuthority('PlatformAdmin', 'PlatformService')")
    public Page<PaymentTransaction> find(String orderId,
                                         List<String> enterpriseIds,
                                         LocalDate startDate,
                                         LocalDate endDate,
                                         List<PaymentStatus> paymentStatuses,
                                         Pageable pagination) {

        Specifications<PaymentTransaction> spec = null;

        if (orderId != null && !orderId.trim().isEmpty()) {
            spec = where(PaymentTransactionSpecs.equalWithOrderId(orderId));
        } else if (enterpriseIds != null) {
            spec = where(PaymentTransactionSpecs.matchTheEnterpriseIds(enterpriseIds));
        }

        if (currentUser.getRole().equals(Role.PlatformAdmin.toString())) {
            List<OrderType> orderTypes = orderTypeAccessHandler.validateAndGetPermittedOrderTypes().stream()
                    .map(OrderType::of).collect(Collectors.toList());

            spec = spec == null
                    ? where(PaymentTransactionSpecs.matchTheOrderTypes(orderTypes))
                    : spec.and(PaymentTransactionSpecs.matchTheOrderTypes(orderTypes));
        }

        if (startDate != null && endDate != null) {

            Instant fromDateStart = startDate.atStartOfDay().atZone(defaultZone).toInstant();
            Instant toDateEnd = endDate.atTime(LocalTime.MAX).atZone(defaultZone).toInstant();
            Range<Instant> paidTimeRange = new Range<>(fromDateStart, toDateEnd);

            spec = spec == null
                    ? where(PaymentTransactionSpecs.inTheRangeOfPaidTime(paidTimeRange))
                    : spec.and(PaymentTransactionSpecs.inTheRangeOfPaidTime(paidTimeRange));

        }

        if (paymentStatuses != null) {
            spec = spec == null
                    ? where(PaymentTransactionSpecs.matchThePaymentStatus(paymentStatuses))
                    : spec.and(PaymentTransactionSpecs.matchThePaymentStatus(paymentStatuses));
        }

        return paymentTransactionRepository.findAll(spec, pagination)
                .map(this::loadEnterpriseName);
    }

    private PaymentTransaction loadEnterpriseName(PaymentTransaction paymentTransaction) {
        paymentTransaction.setEnterpriseName(
                userClient.getEnterpriseNameById(paymentTransaction.getEnterpriseId()));
        return paymentTransaction;
    }

    public List<PaymentTransaction> findByOrderIds(List<String> orderIds) {
        return paymentTransactionRepository.findByOrderIdIn(orderIds);
    }
}
