package fun.hercules.order.order.platform.order.service;

import com.google.common.collect.ImmutableList;
import fun.hercules.order.order.business.acg.client.AcgBusinessClient;
import fun.hercules.order.order.business.acg.domain.AcgOrder;
import fun.hercules.order.order.business.acg.domain.AcgShippingInfo;
import fun.hercules.order.order.business.acg.dto.AcgResponse;
import fun.hercules.order.order.business.acg.dto.booked.AcgBookedRequest;
import fun.hercules.order.order.business.acg.service.AcgOrderService;
import fun.hercules.order.order.clients.service.OrderNotificationService;
import fun.hercules.order.order.common.dto.NotificationType;
import fun.hercules.order.order.common.dto.OrderNotification;
import fun.hercules.order.order.common.dto.OrderNotificationType;
import fun.hercules.order.order.common.errors.ErrorCode;
import fun.hercules.order.order.common.exceptions.BadRequestException;
import fun.hercules.order.order.common.exceptions.ForbiddenException;
import fun.hercules.order.order.common.exceptions.NotFoundException;
import fun.hercules.order.order.platform.exports.BusinessOrder;
import fun.hercules.order.order.platform.order.dto.OrderPageResponse;
import fun.hercules.order.order.platform.order.dto.TransactionNotification;
import fun.hercules.order.order.platform.order.exceptions.PrimaryKeyException;
import fun.hercules.order.order.platform.order.model.CancelReason;
import fun.hercules.order.order.platform.order.model.OperationType;
import fun.hercules.order.order.platform.order.model.OrderBill;
import fun.hercules.order.order.platform.order.model.OrderStatus;
import fun.hercules.order.order.platform.order.model.OrderType;
import fun.hercules.order.order.platform.order.model.Payment;
import fun.hercules.order.order.platform.order.model.PaymentRequest;
import fun.hercules.order.order.platform.order.model.PaymentStatus;
import fun.hercules.order.order.platform.order.repository.OrderBillRepository;
import fun.hercules.order.order.platform.order.repository.OrderStatusRepository;
import fun.hercules.order.order.platform.order.utils.TransitionGraph;
import fun.hercules.order.order.platform.order.validators.ValidatorForUserAccess;
import fun.hercules.order.order.platform.user.CurrentUser;
import fun.hercules.order.order.platform.user.Role;
import fun.hercules.order.order.utils.PaymentUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {

    private static final String PRIMARY_KEY_CONSTRAINT_NAME = "PRIMARY";

    private static final Map<String, String> ORDER_TYPE_MAP = new HashMap<String, String>() {
        {
            put("acg", "航空货运");
            put("wms", "WMS");
        }
    };

    private BusinessOrderHandlers orderHandlers;

    private CurrentUser currentUser;

    public CurrentUser getCurrentUser() {
        return currentUser;
    }

    private OperationLogService operationLogService;

    private ValidatorForUserAccess userAccess;

    private OrderNotificationService orderNotificationService;

    private OrderBillRepository orderBillRepository;


    private OrderStatusRepository orderStatusRepository;

    public OrderService(CurrentUser currentUser,
                        @Lazy BusinessOrderHandlers orderHandlers,
                        OperationLogService operationLogService, ValidatorForUserAccess userAccess,
                        OrderNotificationService orderNotificationService, OrderBillRepository orderBillRepository, OrderStatusRepository orderStatusRepository) {
        this.orderHandlers = orderHandlers;
        this.currentUser = currentUser;
        this.operationLogService = operationLogService;
        this.userAccess = userAccess;
        this.orderNotificationService = orderNotificationService;
        this.orderBillRepository = orderBillRepository;
        this.orderStatusRepository = orderStatusRepository;
    }

    @PreAuthorize("hasAnyAuthority('PlatformAdmin', 'EnterpriseUser', 'PlatformService')")
    public OrderPageResponse<BusinessOrder> list(String enterpriseId, String type, String status, int page, int size) {
        //验证获取order列表的权限
        enterpriseId = ensureAccessToGetOrders(enterpriseId, type);
        PageRequest pageRequest = new PageRequest(page, size);
        return getBusinessHandler(type).listAllByEnterpriseAndStatuses(enterpriseId, status, pageRequest);
    }


    // retry if order id conflicts
    @Transactional
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 100), value = PrimaryKeyException.class)
    @PreAuthorize("hasAnyAuthority('EnterpriseUser', 'PlatformAdmin', 'PlatformService')")
    public BusinessOrder create(String type, String rawOrder) {
        ensureAccessOfPlatformAdmin(type);
        BusinessHandler handler = getBusinessHandler(type);
        try {
            BusinessOrder order = handler.deserialize(rawOrder);
            order.setUserId(currentUser.getUserId());
            order.setEnterpriseId(StringUtils.isEmpty(order.getEnterpriseId()) ? currentUser.getEnterpriseId() : order.getEnterpriseId());
            order.setUserRole(currentUser.getRole());
            order.setStatus(Optional.ofNullable(order.getStatus()).orElse(new OrderStatus(OrderStatus.Type.Submitted)));
            order = handler.create(order);
            //记录操作记录
            operationLogService.log(order, OperationType.Type.Submitted);
            return order;
        } catch (DataIntegrityViolationException e) {
            if (isPrimaryKeyException(e)) {
                throw new PrimaryKeyException(String.format("order id conflicts for %s",
                        handler.getModelType().getSimpleName()), e);
            } else {
                throw e;
            }
        }
    }

    private boolean isPrimaryKeyException(DataIntegrityViolationException ex) {
        if (!(ex.getCause() instanceof ConstraintViolationException)) {
            return false;
        }
        return PRIMARY_KEY_CONSTRAINT_NAME.equals(((ConstraintViolationException) ex.getCause()).getConstraintName());
    }

    @PreAuthorize("hasAnyAuthority('EnterpriseUser', 'PlatformAdmin', 'PlatformService', 'PlatformAdmin')")
    public BusinessOrder get(String type, String id) {
        ensureAccessOfPlatformAdmin(type);
        BusinessOrder order = getById(getBusinessHandler(type), id);
        String enterpriseId = order.getEnterpriseId();
        if (isEnterpriseUser() && (StringUtils.isEmpty(enterpriseId) || !enterpriseId.equals(currentUser.getEnterpriseId()))) {
            throw new ForbiddenException(ErrorCode.ORDER_ACCESS_DENIED,
                    String.format("user doesn't have access privilege to order of %s", enterpriseId));
        }
        return order;
    }


    // update order from raw json
    @Transactional
    @PreAuthorize("hasAnyAuthority('EnterpriseUser', 'PlatformAdmin', 'PlatformService', 'PlatformService')")
    public BusinessOrder update(String type, String orderId, String rawOrder) {
        ensureAccessOfPlatformAdmin(type);
        BusinessHandler handler = getBusinessHandler(type);
        BusinessOrder order = getById(handler, orderId);
        BusinessOrder newOrder = handler.deserialize(rawOrder);
        OrderStatus newStatus = Optional.ofNullable(newOrder.getStatus()).orElse(order.getStatus());
        ensureAccessToUpdate(newStatus);
        order = handler.update(order, newOrder);
        return order;
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('EnterpriseUser', 'PlatformAdmin', 'PlatformService', 'PlatformService')")
    public void updateStatusToOrderTracking(String type, String orderId) {
        ensureAccessOfPlatformAdmin(type);
        BusinessHandler handler = getBusinessHandler(type);
        BusinessOrder order = getById(handler, orderId);
        if (order != null) {
            order.setStatus(new OrderStatus(OrderStatus.Type.OrderTracking));
            orderStatusRepository.save(order.getStatus());
        }
    }


    // update updated order
    @Transactional
//    @PreAuthorize("hasAnyAuthority('EnterpriseUser', 'PlatformAdmin', 'PlatformService', 'PlatformService')")
    public BusinessOrder updateBusinessOrder(BusinessOrder order) {
        BusinessHandler handler = getBusinessHandler(order.getOrderType());
        ensureAccessToUpdate(order.getStatus());
        order = handler.update(order);

        return order;
    }

    @Transactional
    public BusinessOrder updateOrderPayment(String orderId, List<PaymentRequest> paymentRequests) {
        BusinessHandler handler = getBusinessHandlerByOrderId(orderId);
        BusinessOrder order = handler.pay(orderId, PaymentUtils.joinPaymentRequestIds(paymentRequests));
        return order;
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('PlatformAdmin', 'PlatformService')")
    public void delete(String type, String orderId) {
        ensureAccessOfPlatformAdmin(type);
        BusinessHandler handler = getBusinessHandler(type);
        handler.delete(orderId);
    }

    private String ensureAccessToGetOrders(String enterpriseId, String type) {
        // 验证企业用户的权限
        if (isEnterpriseUser()) {
            if (!StringUtils.isEmpty(enterpriseId)) {
                // 企业用户只能查看同一企业下的订单
                if (!enterpriseId.equals(currentUser.getEnterpriseId())) {
                    throw new ForbiddenException(ErrorCode.ORDER_ACCESS_DENIED,
                            String.format("user doesn't have access privilege to order of %s", enterpriseId));
                }
            } else {
                enterpriseId = currentUser.getEnterpriseId();
            }
        }
        // 验证运营管理员的权限
        ensureAccessOfPlatformAdmin(type);
        return enterpriseId;
    }

    private void ensureAccessToUpdate(OrderStatus status) {
        if (status.equals(new OrderStatus(OrderStatus.Type.Paid)) && !isPlatformServiceOrPlatformAdmin()) {
            throw new ForbiddenException(ErrorCode.ORDER_ACCESS_DENIED, "user doesn't have access privilege to order");
        }
    }

    // replaced by getByOrderId(String orderId)
    @Deprecated
    private BusinessOrder getById(BusinessHandler handler, String id) {
        return handler.find(id).orElseThrow(() ->
                new NotFoundException(ErrorCode.ORDER_NOT_FOUND, String.format("can't find order by id %s", id)));
    }

    private BusinessHandler getBusinessHandler(String type) {
        return orderHandlers.getByType(type);
    }

    private BusinessHandler getBusinessHandlerByOrderId(String orderId) {
        return orderHandlers.getByOrderId(orderId);
    }

    private <T extends BusinessOrder> T ensureAccessPrivilege(T order) {
        // 验证企业用户的权限，企业用户只能查看同一企业下的订单
        if (Role.valueOf(currentUser.getRole()).equals(Role.EnterpriseUser)
                && !Optional.ofNullable(currentUser.getEnterpriseId()).orElse("").equals(order.getEnterpriseId())) {
            throw new ForbiddenException(ErrorCode.ORDER_ACCESS_DENIED,
                    String.format("user doesn't have access privilege to order under enterprise %s", order.getEnterpriseId()));
        }
        return order;
    }

    private boolean isEnterpriseUser() {
        return currentUser.getRole().equals(Role.EnterpriseUser.name());
    }

    private void ensureAccessOfPlatformAdmin(String type) {
        if (currentUser.getRole().equals(Role.PlatformAdmin.name())) {
            //各个业态下的运营管理员只能操作各个业态下的订单
            userAccess.validateAccess(type, currentUser);
        }
    }

    public boolean isPlatformServiceOrPlatformAdmin() {
        String role = currentUser.getRole();
        return role.equals(Role.PlatformService.name()) || role.equals(Role.PlatformAdmin.name());
    }

    public BusinessOrder getByOrderId(String orderId) {
        return getBusinessHandlerByOrderId(orderId).find(orderId).orElseThrow(() ->
                new NotFoundException(ErrorCode.ORDER_NOT_FOUND, String.format("can't find order by id %s", orderId)));
    }

    @PreAuthorize("hasAnyAuthority('EnterpriseUser', 'PlatformAdmin', 'PlatformService', 'PlatformService')")
    public List<BusinessOrder> listOrderByTypeAndIds(String type, Set<String> ids) {
        //验证运营admin的权限
        ensureAccessOfPlatformAdmin(type);
        List<BusinessOrder> orders = listOrderByAndIds(type, ids);

        orders.stream()
                .filter(order -> order != null)
                .forEach(this::ensureAccessPrivilege);
        return orders;

    }

    public List<BusinessOrder> listAcgOrderByIds(Set<String> ids) {
        return listOrderByAndIds(OrderType.Type.ACG.getValue(), ids);
    }

    private List<BusinessOrder> listOrderByAndIds(String type, Set<String> ids) {
        // 验证ids
        ids.stream().filter(StringUtils::isEmpty)
                .findAny()
                .ifPresent(s -> {
                    throw new BadRequestException(ErrorCode.INVALID_ORDER_ID,
                            String.format("ids %s contains empty value", ids));
                });

        // build result set
        return getBusinessHandler(type).findAll(ids)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('PlatformService')")
    public void handleOrderAfterPayment(Payment payment, String operatorId) {
        log.info("handle payment {}", payment.getId());
        Map<String, List<PaymentRequest>> paymentsByOrderId = payment.getPaymentRequests().stream()
                .collect(Collectors.groupingBy(pr -> pr.getOrderId()));

        if (payment.getPayMethod().equalsIgnoreCase("OFFLINE")) {
            paymentsByOrderId.forEach((orderId, paymentRequests) -> updateOrderAfterOfflinePayments(orderId, paymentRequests, payment, operatorId));
        } else {
            paymentsByOrderId.forEach((orderId, paymentRequests) -> updateOrderAfterOnlinePayments(orderId, paymentRequests, payment.getPayUserId()));
        }
    }

    @PreAuthorize("hasAnyAuthority('PlatformService')")
    public void handleOrderAfterRefund(BusinessOrder order, String operatorId) {
        order.setRefundStatus(true);
        order = updateBusinessOrder(order);
        operationLogService.paymentLog(order, OperationType.Type.Refunded, operatorId);

        ImmutableList<OrderStatus> statuses = ImmutableList.of(
                OrderStatus.of(OrderStatus.Type.Paid),
                OrderStatus.of(OrderStatus.Type.OrderTracking)
        );

        if (statuses.contains(order.getStatus())) {
            log.info("update order {} status", order.getStatus());
            cancel(order.getOrderType(), order.getId(), CancelReason.of(CancelReason.Type.Refund));
        }
    }

    private void updateOrderAfterOnlinePayments(String orderId, List<PaymentRequest> paymentRequests, String payUserId) {
        // TODO: 2017/12/20 need to handle Success & Fail payment
        log.info("update order {} payments", orderId);
        BusinessOrder order = updateOrderPayment(orderId, paymentRequests);
        updateOrderBillWithSuccessStatus(orderId);
        operationLogService.paymentLog(order, OperationType.Type.Paid, payUserId);
    }

    private void updateOrderAfterOfflinePayments(String orderId, List<PaymentRequest> paymentRequests, Payment payment, String operatorId) {
        // TODO: 2017/12/20 need to handle Success & Fail payment
        log.info("update order {} payments", orderId);

        BusinessOrder order = getByOrderId(orderId);
        if (payment.getStatus().equals(PaymentStatus.Success)) {
            order = updateOrderPayment(orderId, paymentRequests);
            updateOrderBillWithSuccessStatus(orderId);
            operationLogService.paymentLog(order, OperationType.Type.OfflinePaidConfirmSuccess, operatorId);
        }

        if (payment.getStatus().equals(PaymentStatus.Fail)) {
            order.setStatus(OrderStatus.of(OrderStatus.Type.WaitForPay));
            order = updateBusinessOrder(order);
            operationLogService.paymentLog(order, OperationType.Type.OfflinePaidConfirmFail, operatorId);
            orderNotificationService.create(buildOfflinePaymentFailedNotification(order, generateOrderInfo(order)));
        }
    }

    private String generateOrderInfo(BusinessOrder order) {
        String orderInfo = "";
        if ("acg".equalsIgnoreCase(order.getOrderType())) {
            AcgShippingInfo shippingInfo = ((AcgOrder) order).getShippingInfo();
            orderInfo = String.format("%s - %s", shippingInfo.getDeparture().getAirportName(), shippingInfo.getArrival().getAirportName());
        }
        return orderInfo;
    }

    private OrderNotification buildOfflinePaymentFailedNotification(BusinessOrder order, String orderInfo) {
        return OrderNotification.builder()
                .orderId(order.getId())
                .enterpriseId(order.getEnterpriseId())
                .notificationType(NotificationType.OrderNotification)
                .name(OrderNotificationType.OfflinePayFailureNotification.getName())
                .orderType(order.getOrderType())
                .orderInfo(orderInfo)
                .status(order.getStatus().getName())
                .description(String.format("您的%s订单%s，线下支付审核未通过，请立即前往查看。", ORDER_TYPE_MAP.get(order.getOrderType().toLowerCase()), order.getId()))
                .serviceTypes(String.join(",", order.getOrderSubTypes()))
                .build();
    }

    private void updateOrderBillWithSuccessStatus(String orderId) {
        Optional<OrderBill> orderBillOptional = orderBillRepository.findByOrderId(orderId);
        if (orderBillOptional.isPresent()) {
            OrderBill orderBill = orderBillOptional.get();
            orderBill.paySuccess();
            orderBillRepository.save(orderBill);
        } else {
            log.warn("miss order bill record in database about orderId {}", orderId);
        }
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('EnterpriseUser', 'PlatformService', 'PlatformAdmin', 'PlatformService', 'PlatformAdmin')")
    public BusinessOrder cancel(String type, String orderId, CancelReason cancelReason) {
        ensureAccessOfPlatformAdmin(type);
        BusinessOrder order = getBusinessHandler(type).cancel(orderId, cancelReason);
        operationLogService.log(order, OperationType.Type.Cancelled);

        return order;
    }

    public String renderOrderTransitions(String type) {
        return new TransitionGraph(getBusinessHandler(type).getStatusTransitions()).render();
    }

    public void createRefundNotification(BusinessOrder order, TransactionNotification notification) {
        ImmutableList<String> orderTypes = ImmutableList.of("ACG", "WMS");

        if (orderTypes.contains(order.getOrderType().toUpperCase())) {
            String currencySymbol = notification.getCurrency().getSymbol(Locale.CHINA); // TODO: change Locale.CHINA when supporting multiple currency
            BigDecimal payAmount = notification.getPayAmount();
            orderNotificationService.create(buildRefundOrderNotification(order, currencySymbol, payAmount, generateOrderInfo(order)));
        }
    }

    private OrderNotification buildRefundOrderNotification(BusinessOrder order, String currencySymbol, BigDecimal payAmount, String orderInfo) {
        return OrderNotification.builder()
                .orderId(order.getId())
                .enterpriseId(order.getEnterpriseId())
                .notificationType(NotificationType.OrderNotification)
                .name(OrderNotificationType.RefundSuccessNotification.getName())
                .orderType(order.getOrderType())
                .orderInfo(orderInfo)
                .status(order.getStatus().getName())
                .description(String.format("您的%s订单%s已退款，退款金额：%s%s, 请知悉。", ORDER_TYPE_MAP.get(order.getOrderType().toLowerCase()), order.getId(), currencySymbol, payAmount))
                .serviceTypes(String.join(",", order.getOrderSubTypes()))
                .build();
    }
}
