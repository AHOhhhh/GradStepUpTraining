package fun.hercules.order.order.business.wms.service;

import fun.hercules.order.order.business.wms.constants.WmsDefaultInformation;
import fun.hercules.order.order.business.wms.domain.WmsOrder;
import fun.hercules.order.order.business.wms.domain.WmsOrderType;
import fun.hercules.order.order.business.wms.repository.WmsOrderRepository;
import fun.hercules.order.order.business.wms.repository.custom.MixFilterSpec;
import fun.hercules.order.order.clients.service.OrderNotificationService;
import fun.hercules.order.order.common.annotations.StatusHandler;
import fun.hercules.order.order.common.constants.ConfigProperties;
import fun.hercules.order.order.common.dto.NotificationType;
import fun.hercules.order.order.common.dto.OrderNotification;
import fun.hercules.order.order.common.dto.OrderNotificationType;
import fun.hercules.order.order.common.errors.ErrorCode;
import fun.hercules.order.order.common.exceptions.BadRequestException;
import fun.hercules.order.order.common.exceptions.ConflictException;
import fun.hercules.order.order.platform.exports.BusinessOrderType;
import fun.hercules.order.order.platform.exports.StatusChange;
import fun.hercules.order.order.platform.exports.StatusTransitions;
import fun.hercules.order.order.platform.exports.service.BusinessOrderServiceBase;
import fun.hercules.order.order.platform.order.dto.OrderPageResponse;
import fun.hercules.order.order.platform.order.model.CancelReason;
import fun.hercules.order.order.platform.order.model.OperationType;
import fun.hercules.order.order.platform.order.model.OrderStatus;
import fun.hercules.order.order.platform.order.service.OperationLogService;
import fun.hercules.order.order.platform.order.service.PaymentRequestService;
import fun.hercules.order.order.platform.user.CurrentUser;
import fun.hercules.order.order.platform.user.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static fun.hercules.order.order.business.wms.domain.WmsOrderType.Type.Open;
import static fun.hercules.order.order.business.wms.domain.WmsOrderType.Type.Recharge;

@Slf4j
@Service
@BusinessOrderType(WmsOrder.class)
public class WmsOrderService extends BusinessOrderServiceBase<WmsOrder> {

    private final WmsOrderRepository orderRepository;

    private final PaymentRequestService paymentRequestService;

    private final OperationLogService logService;

    private final OrderNotificationService orderNotificationService;

    private final StatusTransitions statusTransitions = StatusTransitions.builder()
            .transition(OrderStatus.Type.Submitted, OrderStatus.Type.Audited, Role.PlatformAdmin)
            .transition(OrderStatus.Type.Submitted, OrderStatus.Type.Cancelled, Role.EnterpriseUser)
            // for wms recharge order
            .transition(OrderStatus.Type.Submitted, OrderStatus.Type.Paid, Role.PlatformService)
            .transition(OrderStatus.Type.Submitted, OrderStatus.Type.OfflinePaidAwaitingConfirm, Role.EnterpriseUser)
            .transition(OrderStatus.Type.OfflinePaidAwaitingConfirm, OrderStatus.Type.Submitted, Role.PlatformService)
            .transition(OrderStatus.Type.Audited, OrderStatus.Type.Paid, Role.PlatformService, Role.Internal)
            .transition(OrderStatus.Type.Audited, OrderStatus.Type.OfflinePaidAwaitingConfirm, Role.EnterpriseUser)
            .transition(OrderStatus.Type.OfflinePaidAwaitingConfirm, OrderStatus.Type.Paid, Role.PlatformService)
            .transition(OrderStatus.Type.OfflinePaidAwaitingConfirm, OrderStatus.Type.Audited, Role.PlatformService)
            .transition(OrderStatus.Type.Audited, OrderStatus.Type.Cancelled, Role.EnterpriseUser)
            .transition(OrderStatus.Type.Paid, OrderStatus.Type.Closed, Role.Internal)
            .transition(OrderStatus.Type.Paid, OrderStatus.Type.Cancelled, Role.PlatformService)
            .build();

    public WmsOrderService(WmsOrderRepository orderRepository,
                           PaymentRequestService paymentRequestService,
                           OperationLogService logService,
                           CurrentUser currentUser,
                           OrderNotificationService orderNotificationService) {
        super(currentUser);
        this.orderRepository = orderRepository;
        this.logService = logService;
        this.paymentRequestService = paymentRequestService;
        this.orderNotificationService = orderNotificationService;
    }

    @Override
    public Optional<WmsOrder> findOrder(String orderId) {
        return orderRepository.findOneById(orderId);
    }


    @Override
    protected WmsOrder onPaid(WmsOrder order, String paymentRequestIds) {
        return orderRepository.save(order);
    }

    @Override
    protected WmsOrder onCancel(WmsOrder order, CancelReason cancelReason) {
        return orderRepository.save(order);
    }

    @Override
    protected WmsOrder onUpdate(WmsOrder order, StatusChange statusChange) {
        return orderRepository.save(order);
    }

    @StatusHandler(OrderStatus.Type.Audited)
    protected WmsOrder onAudited(WmsOrder order, StatusChange statusChange) {
        assert (statusChange.isChanged());
        assert (statusChange.getNewStatus().getType().equals(OrderStatus.Type.Audited));
        if (order.getType().getType() != Recharge) {
            paymentRequestService.createAndSavePaymentRequest(order);
            orderNotificationService.create(buildWaitForPayNotification(order));
        }

        if (StringUtils.isEmpty(order.getServiceIntro())) {
            order.setServiceIntro("");
        }
        logService.log(order, OperationType.Type.Audited);

        return orderRepository.save(order);
    }


    @Override
    public WmsOrder createOrder(WmsOrder order) {
        Optional<WmsOrder> existingOrder = findExistingOrder(order);
        if (order.getType().getType() == Open) {
            //一个企业只能开通一个wms订单
            if (existingOrder.isPresent()) {
                throw new ConflictException(ErrorCode.CREATE_ORDER_ERROR);
            }
            //subscriptionId用于开通、充值、续费订单之间的关联
            order.setSubscriptionId(UUID.randomUUID().toString());
        } else {
            //为充值、续费订单设置subscriptionId
            Optional<String> subscriptionId = existingOrder.map(WmsOrder::getSubscriptionId);
            if (StringUtils.isEmpty(subscriptionId.orElse(null))) {
                throw new BadRequestException(ErrorCode.WMS_OPEN_ORDER_NOT_FOUND);
            } else {
                order.setSubscriptionId(subscriptionId.get());
            }
        }

        initDefaultFields(order);
        //订单期限为１年
        order.setEffectiveFrom(Instant.now().atZone(ConfigProperties.getInstance().getTimeZone()).toLocalDate());
        order.setEffectiveTo(order.getEffectiveFrom().plus(1, ChronoUnit.YEARS));

        WmsOrder savedOrder = orderRepository.save(order);

        // No need to audit wms recharge order and need to pay directly
        if (savedOrder.getStatus().equals(OrderStatus.of(OrderStatus.Type.Submitted)) && savedOrder.getType().getType() == Recharge) {
            //创建支付请求
            paymentRequestService.createAndSavePaymentRequest(savedOrder);
            orderNotificationService.create(buildWaitForPayNotification(savedOrder));
        }
        return savedOrder;
    }

    private void initDefaultFields(WmsOrder order) {
        WmsDefaultInformation defaultInformation = WmsDefaultInformation.getInstance();
        if (order.getMinPrice() == null) {
            order.setMinPrice(defaultInformation.getMinPrice());
        }
        if (order.getMaxPrice() == null) {
            order.setMaxPrice(defaultInformation.getMaxPrice());
        }
        if (StringUtils.isEmpty(order.getServiceIntro())) {
            order.setServiceIntro(defaultInformation.getDescribe());
        }
    }

    private Optional<WmsOrder> findExistingOrder(WmsOrder order) {
        List<WmsOrder> orders = orderRepository.findAllByEnterpriseIdAndTypeIsAndStatusIsNot(
                order.getEnterpriseId(), new WmsOrderType(Open), OrderStatus.of(OrderStatus.Type.Cancelled));
        return orders.isEmpty() ? Optional.empty() : Optional.ofNullable(orders.get(0));
    }

    private OrderNotification buildWaitForPayNotification(WmsOrder order) {
        return OrderNotification.builder()
                .orderId(order.getId())
                .enterpriseId(order.getEnterpriseId())
                .notificationType(NotificationType.OrderNotification)
                .name(OrderNotificationType.WaitForPayNotification.getName())
                .orderType(order.getOrderType())
                .orderInfo("")
                .status(order.getStatus().getName())
                .description(String.format("您的WMS订单%s，订单价格已确认，请立即前往支付。", order.getId()))
                .serviceTypes(String.join(",", order.getOrderSubTypes()))
                .build();
    }

    @Override
    public StatusTransitions getStatusTransitions() {
        return statusTransitions;
    }

    @Override
    public OrderPageResponse<WmsOrder> listByEnterpriseAndStatuses(String enterpriseId, String status, PageRequest pageRequest) {
        Page<WmsOrder> ordersPage = orderRepository.findAll(new MixFilterSpec(enterpriseId, status), pageRequest);
        return new OrderPageResponse<>(ordersPage.getContent(), ordersPage.getTotalPages(), pageRequest.getPageNumber(), pageRequest.getPageSize(), ordersPage.getTotalElements());
    }

    @Override
    public List<WmsOrder> onFindAll(Set<String> orderIds) {
        return orderRepository.findAllByIdIn(orderIds);
    }
}
