package fun.hercules.order.order.business.acg.service;

import fun.hercules.order.order.business.acg.domain.AcgCompleteRequest;
import fun.hercules.order.order.business.acg.domain.AcgOrder;
import fun.hercules.order.order.business.acg.domain.AcgShippingInfo;
import fun.hercules.order.order.business.acg.domain.LogisticsStatus;
import fun.hercules.order.order.business.acg.domain.UpdateLogisticStatusRequest;
import fun.hercules.order.order.business.acg.dto.AcgResponse;
import fun.hercules.order.order.business.acg.repository.AcgOrderRepository;
import fun.hercules.order.order.business.acg.repository.custom.MixFilterSpec;
import fun.hercules.order.order.clients.service.OrderNotificationService;
import fun.hercules.order.order.common.annotations.StatusHandler;
import fun.hercules.order.order.common.constants.DateTimeConstants;
import fun.hercules.order.order.common.dto.NotificationType;
import fun.hercules.order.order.common.dto.OrderNotification;
import fun.hercules.order.order.common.dto.OrderNotificationType;
import fun.hercules.order.order.platform.exports.BusinessOrderType;
import fun.hercules.order.order.platform.exports.StatusChange;
import fun.hercules.order.order.platform.exports.StatusTransitions;
import fun.hercules.order.order.platform.exports.service.BusinessOrderServiceBase;
import fun.hercules.order.order.platform.order.dto.OrderPageResponse;
import fun.hercules.order.order.platform.order.model.CancelReason;
import fun.hercules.order.order.platform.order.model.OrderLogistic;
import fun.hercules.order.order.platform.order.model.OrderStatus;
import fun.hercules.order.order.platform.order.model.Payment;
import fun.hercules.order.order.platform.order.model.PaymentRequest;
import fun.hercules.order.order.platform.order.model.PaymentStatus;
import fun.hercules.order.order.platform.order.repository.OrderLogisticRepository;
import fun.hercules.order.order.platform.order.service.PaymentRequestService;
import fun.hercules.order.order.platform.order.service.PaymentService;
import fun.hercules.order.order.platform.user.CurrentUser;
import fun.hercules.order.order.platform.user.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@BusinessOrderType(AcgOrder.class)
public class AcgOrderService extends BusinessOrderServiceBase<AcgOrder> {

    private final AcgOrderRepository orderRepository;

    // Defines if need to integrate with yzra/mock-server
    private final boolean integrationEnabled;

    private final StatusTransitions statusTransitions = StatusTransitions.builder()
            .transition(OrderStatus.Type.Submitted, OrderStatus.Type.WaitForPay, Role.PlatformService)
            .transition(OrderStatus.Type.Submitted, OrderStatus.Type.Cancelled, Role.PlatformService, Role.EnterpriseUser)
            .transition(OrderStatus.Type.WaitForPay, OrderStatus.Type.Cancelled, Role.EnterpriseUser)
            .transition(OrderStatus.Type.WaitForPay, OrderStatus.Type.Paid, Role.PlatformService, Role.Internal)
            .transition(OrderStatus.Type.WaitForPay, OrderStatus.Type.OfflinePaidAwaitingConfirm, Role.EnterpriseUser)
            .transition(OrderStatus.Type.OfflinePaidAwaitingConfirm, OrderStatus.Type.Paid, Role.PlatformService)
            .transition(OrderStatus.Type.OfflinePaidAwaitingConfirm, OrderStatus.Type.OrderTracking, Role.PlatformService)
            .transition(OrderStatus.Type.OfflinePaidAwaitingConfirm, OrderStatus.Type.WaitForPay, Role.PlatformService)
            .transition(OrderStatus.Type.Paid, OrderStatus.Type.OrderTracking, Role.Internal)
            .transition(OrderStatus.Type.Paid, OrderStatus.Type.Cancelled, Role.PlatformService)
            .transition(OrderStatus.Type.OrderTracking, OrderStatus.Type.Closed, Role.PlatformService)
            .transition(OrderStatus.Type.OrderTracking, OrderStatus.Type.Cancelled, Role.PlatformService)
            .build();

    private OrderLogisticRepository orderLogisticRepository;

    private PaymentRequestService paymentRequestService;

    private PaymentService paymentService;

    private AcgOrderViewConverter viewConverter;

    private OrderNotificationService orderNotificationService;

    public AcgOrderService(AcgOrderRepository orderRepository,
                           PaymentRequestService paymentRequestService,
                           PaymentService paymentService,
                           @Value("${hlp.business-services.acg.integration.enabled}") boolean integrationEnabled,
                           AcgAirportService airportService,
                           CurrentUser currentUser,
                           OrderNotificationService orderNotificationService,
                           OrderLogisticRepository orderLogisticRepository) {
        super(currentUser);
        this.orderRepository = orderRepository;
        this.paymentRequestService = paymentRequestService;
        this.paymentService = paymentService;
        this.integrationEnabled = integrationEnabled;
        viewConverter = new AcgOrderViewConverter(airportService);
        this.orderNotificationService = orderNotificationService;
        this.orderLogisticRepository = orderLogisticRepository;
    }

    @Override
    public Optional<AcgOrder> findOrder(String orderId) {
        return orderRepository.findOneById(orderId).map(viewConverter::convert);
    }

    @Override
    protected AcgOrder onPaid(AcgOrder order, String paymentRequestIds) {
        // The paid status of ACG order is orderTracking
        order.setStatus(OrderStatus.of(OrderStatus.Type.OrderTracking));

        AcgOrder updatedOrder = saveOrder(order);
        notifyPaymentStatus(updatedOrder, paymentRequestIds);

        return updatedOrder;
    }

    @StatusHandler(OrderStatus.Type.WaitForPay)
    protected AcgOrder onWaitForPay(AcgOrder order, StatusChange statusChange) {
        paymentRequestService.createAndSavePaymentRequest(order);

        orderNotificationService.create(buildWaitForPayNotification(order));
        return saveOrder(order);
    }

    private OrderNotification buildWaitForPayNotification(AcgOrder order) {
        AcgShippingInfo shippingInfo = order.getShippingInfo();

        return OrderNotification.builder()
                .orderId(order.getId())
                .enterpriseId(order.getEnterpriseId())
                .notificationType(NotificationType.OrderNotification)
                .name(OrderNotificationType.WaitForPayNotification.getName())
                .orderType(order.getOrderType())
                .orderInfo(String.format("%s - %s", shippingInfo.getDeparture().getAirportName(), shippingInfo.getArrival().getAirportName()))
                .status(order.getStatus().getName())
                .description(String.format("您的航空货运订单%s, 舱位已预订成功，请立即前往支付。", order.getId()))
                .serviceTypes(String.join(",", order.getOrderSubTypes()))
                .build();
    }

    private AcgOrder saveOrder(AcgOrder order) {
        return orderRepository.save(bindOrderFields(order));
    }

    @Override
    protected AcgOrder onCancel(AcgOrder order, CancelReason cancelReason) {
        throw new UnsupportedOperationException();
    }


    @Override
    protected AcgOrder onUpdate(AcgOrder order, StatusChange statusChange) {
        return saveOrder(order);
    }

    public void notifyPaymentStatus(AcgOrder acgOrder, String paymentRequestIds) {
        if (integrationEnabled) {
            List<PaymentRequest> paymentRequests = paymentRequestService.findAllPaymentRequestsByOrderId(acgOrder.getId()).stream()
                    .filter(paymentRequest -> PaymentStatus.Success.equals(paymentRequest.getPaymentStatus()))
                    .collect(Collectors.toList());
            PaymentRequest paymentRequest = paymentRequests.get(0);
            List<Payment> payments = paymentService.findPayments(paymentRequests).stream()
                    .filter(payment -> PaymentStatus.Success.equals(payment.getStatus()))
                    .collect(Collectors.toList());
            Instant paidTime = paymentRequest.getPaidTime();
            String paymentTransaction = paymentRequestIds;
            Currency currency = paymentRequest.getCurrency();
            BigDecimal amount = paymentRequest.getAmount();
            String payMethod = payments.get(0).getPayMethod();

            // TODO - integrate with acg provider
            log.info("order {} paid on {}, transaction:{} amount:{} currency:{} method:{}",
                    acgOrder.getId(), paidTime.atZone(ZoneId.of(DateTimeConstants.TIME_ZONE)).toString(),
                    paymentTransaction, amount, currency, payMethod);
        }
    }

    @Override
    public AcgOrder createOrder(AcgOrder order) {
        order.setStatus(OrderStatus.of(OrderStatus.Type.Submitted));
        order = saveOrder(calculatePrice(order));
        String delegateOrderId = "TODO";
        if (null != delegateOrderId) {
            order.setDelegateOrderId(delegateOrderId);
        }
        return orderRepository.save(order);
    }

    private AcgOrder calculatePrice(AcgOrder order) {
        AcgOrder.Price price = order.getPrice();
        if (price != null) {
            price.setTotal(price.getAirlineFee()
                    .add(price.getDropOffFee())
                    .add(price.getPickUpFee()));
        }
        return order;
    }

    private AcgOrder bindOrderFields(AcgOrder order) {
        Optional.ofNullable(order.getGoods()).ifPresent(acgGoods ->
                acgGoods.forEach(goods -> goods.setOrder(order))
        );
        return order;
    }

    @Override
    public List<AcgOrder> onFindAll(Set<String> orderIds) {
        return orderRepository.findAllByIdIn(orderIds).stream()
                .map(viewConverter::convert)
                .collect(Collectors.toList());
    }

    public PaymentRequest getPaymentRequest(String orderId) {
        // TODO: in the future one order may have multiple payment requests
        List<PaymentRequest> paymentRequestList = paymentRequestService.findAllPaymentRequestsByOrderId(orderId);
        if (paymentRequestList.size() > 0) {
            return paymentRequestService.findAllPaymentRequestsByOrderId(orderId).get(0);
        } else {
            return null;
        }
    }

    @Override
    public StatusTransitions getStatusTransitions() {
        return statusTransitions;
    }

    @Override
    public OrderPageResponse<AcgOrder> listByEnterpriseAndStatuses(String enterpriseId, String status, PageRequest pageRequest) {
        Page<AcgOrder> acgOrderPage = orderRepository.findAll(new MixFilterSpec(enterpriseId,status), pageRequest);
        List<AcgOrder> acgOrders = acgOrderPage.getContent().stream()
                .map(viewConverter::convert)
                .collect(Collectors.toList());
        return new OrderPageResponse<>(acgOrders, acgOrderPage.getTotalPages(), pageRequest.getPageNumber(), pageRequest.getPageSize(), acgOrderPage.getTotalElements());
    }

    public AcgResponse updateLogisticsStatus(String orderId, UpdateLogisticStatusRequest request) {
        List<LogisticsStatus> logisticsStatuses = request.getLogisticsStatus();
        logisticsStatuses.stream().forEach(logisticsStatus -> {
            OrderLogistic orderLogistic = new OrderLogistic(logisticsStatus.getLogisticsStatusInfo(),
                    logisticsStatus.getUpdateInfoTime(),
                    logisticsStatus.getUpdateInfoUserName(),
                    orderId);
            orderLogisticRepository.save(orderLogistic);
        });
        return AcgResponse.success();
    }

    public AcgResponse onComplete(String orderId, AcgCompleteRequest request) {
        if (request.getMessage().equals("order closed")) {
            Optional<AcgOrder> order = orderRepository.findOneById(orderId);
            if (order.isPresent()) {
                AcgOrder acgOrder = order.get();
                acgOrder.setStatus(OrderStatus.of(OrderStatus.Type.Closed));
                orderRepository.save(acgOrder);
                return AcgResponse.success();
            }
        }
        return AcgResponse.failure();
    }

    public static class AcgOrderViewConverter implements Converter<AcgOrder, AcgOrder> {
        private final AcgAirportService airportService;

        public AcgOrderViewConverter(AcgAirportService airportService) {
            this.airportService = airportService;
        }

        @Override
        public AcgOrder convert(AcgOrder order) {
            Optional.ofNullable(order.getShippingInfo()).ifPresent(acgShippingInfo -> {
                Optional.ofNullable(acgShippingInfo.getArrival()).ifPresent(airport -> {
                    airport.setAirportName(airportService.getAirportName(airport.getAirportId()));
                    airport.setAbroad(airportService.getAboard(airport.getAirportId()));
                });
                Optional.ofNullable(acgShippingInfo.getDeparture()).ifPresent(airport -> {
                    airport.setAirportName(airportService.getAirportName(airport.getAirportId()));
                    airport.setAbroad(airportService.getAboard(airport.getAirportId()));
                });
            });
            return order;
        }
    }
}
