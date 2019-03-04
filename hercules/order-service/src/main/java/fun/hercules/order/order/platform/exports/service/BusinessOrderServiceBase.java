package fun.hercules.order.order.platform.exports.service;

import fun.hercules.order.order.common.annotations.StatusHandler;
import fun.hercules.order.order.common.errors.ErrorCode;
import fun.hercules.order.order.common.exceptions.BadRequestException;
import fun.hercules.order.order.common.exceptions.ForbiddenException;
import fun.hercules.order.order.common.exceptions.NotFoundException;
import fun.hercules.order.order.platform.exports.BusinessOrder;
import fun.hercules.order.order.platform.exports.BusinessOrderBase;
import fun.hercules.order.order.platform.exports.BusinessOrderService;
import fun.hercules.order.order.platform.exports.BusinessOrderType;
import fun.hercules.order.order.platform.exports.StatusChange;
import fun.hercules.order.order.platform.exports.StatusTransitions;
import fun.hercules.order.order.platform.order.model.CancelReason;
import fun.hercules.order.order.platform.order.model.OrderStatus;
import fun.hercules.order.order.platform.user.CurrentUser;
import fun.hercules.order.order.platform.user.Role;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Slf4j
public abstract class BusinessOrderServiceBase<T extends BusinessOrderBase> implements BusinessOrderService<T> {
    @Getter
    private final CurrentUser currentUser;

    private Class<? extends BusinessOrder> orderType;

    private Map<OrderStatus.Type, BiFunction<T, StatusChange, T>> orderStatusHandlers;

    protected BusinessOrderServiceBase(CurrentUser currentUser) {
        this.currentUser = currentUser;
        orderType = this.getClass().getDeclaredAnnotation(BusinessOrderType.class).value();
        buildStatusHandlerMapping();
    }

    @Override
    public List<T> findAll(Set<String> orderIds) {
        return onFindAll(orderIds);
    }

    @Override
    public final Optional<T> find(String orderId) {
        return findOrder(orderId);
    }

    @Override
    public final T create(T order) {
        // TODO - should create by front end or
        if (StringUtils.isEmpty(order.getVendor())) {
            order.setVendor(getDefaultVendorByType(order.getOrderType()));
        }
        return createOrder(order);
    }

    private String getDefaultVendorByType(String orderType) {
        switch (orderType) {
            case "acg":
                return "yzra";
            case "wms":
                return "wms";
            default:
                throw new IllegalArgumentException(String.format("illegal order type %s", orderType));
        }
    }

    private void buildStatusHandlerMapping() {
        final BusinessOrderServiceBase<T> self = this;
        orderStatusHandlers = Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(this::isStatusHandler)
                .collect(Collectors.toMap(
                        method -> method.getAnnotation(StatusHandler.class).value(),
                        method -> {
                            log.info("register status handler {}:{} - {}::{}", orderType.getSimpleName(),
                                    method.getAnnotation(StatusHandler.class).value(),
                                    method.getDeclaringClass().getSimpleName(), method.getName());
                            return (order, statusChange) -> {
                                try {
                                    method.setAccessible(true);
                                    return (T) method.invoke(self, order, statusChange);
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    throw new RuntimeException(e);
                                }
                            };
                        }
                ));
    }

    private boolean isStatusHandler(Method method) {
        if (method.isAnnotationPresent(StatusHandler.class)) {
            if (!method.getReturnType().equals(orderType)) {
                throw new IllegalArgumentException(String.format("invalid return type for status handler %s", method.getName()));
            }
            if (method.getParameterCount() != 2) {
                throw new IllegalArgumentException(String.format("invalid parameter count for status handler %s", method.getName()));
            }
            if (!method.getParameterTypes()[0].equals(orderType)) {
                throw new IllegalArgumentException(String.format("invalid parameter[0] for status handler %s", method.getName()));
            }
            if (!method.getParameterTypes()[1].equals(StatusChange.class)) {
                throw new IllegalArgumentException(String.format("invalid parameter[1] for status handler %s", method.getName()));
            }
            return true;
        }
        return false;
    }

    public final T get(String orderId) {
        return find(orderId).orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND,
                String.format("order %s not found", orderId)));
    }

    @Override
    @Transactional
    public T update(T order) {
        StatusChange statusChange = getStatusChangeFromNewOrder(order);
        if (statusChange.isChanged()) {
            switch (statusChange.getNewStatus().getType()) {
                case Paid:
                    throw new BadRequestException(ErrorCode.INVALID_STATUS_TRANSITION,
                            String.format("should update payment via BusinessOrderService::pay()"));
                case Cancelled:
                    throw new BadRequestException(ErrorCode.INVALID_STATUS_TRANSITION,
                            String.format("should cancel order via BusinessOrderService::cancel()"));
                default:
                    //验证订单状态的变化
                    validateStatusTransition(order, statusChange);
                    Optional<BiFunction<T, StatusChange, T>> handler = Optional.ofNullable(orderStatusHandlers.get(order.getStatus().getType()));
                    if (handler.isPresent()) {
                        return handler.get().apply(order, statusChange);
                    }
                    break;
            }
        }
        return onUpdate(order, statusChange);
    }

    private void validateStatusTransition(T order, StatusChange statusChange) {
        StatusTransitions statusTransitions = getStatusTransitions();
        Optional<Set<Role>> legalRoles = statusTransitions.getLegalRoles(
                statusChange.getOldStatus().getType(), statusChange.getNewStatus().getType());
        if (!legalRoles.isPresent() || legalRoles.get().isEmpty()) {
            throw new BadRequestException(ErrorCode.INVALID_STATUS_TRANSITION,
                    String.format("invalid status transition from %s to %s",
                            statusChange.getOldStatus(), statusChange.getNewStatus()));
        }
        validatePrivileges(order, legalRoles.get());
    }

    private void validatePrivileges(T order, Set<Role> roles) {
        try {
            Role currentRole = Role.valueOf(getCurrentUser().getRole());
            if (!roles.contains(currentRole)) {
                throw new ForbiddenException(ErrorCode.ORDER_ACCESS_DENIED,
                        String.format("can't update order %s to %s with role %s(legal roles:%s)",
                                order.getId(), order.getStatus(), currentRole, roles));
            }
            switch (currentRole) {
                case EnterpriseAdmin:
                case EnterpriseUser:
                    if (!isSameEnterprise(getCurrentUser().getEnterpriseId(), order.getEnterpriseId())) {
                        throw new ForbiddenException(ErrorCode.ORDER_ACCESS_DENIED,
                                String.format("can't update order %s to %s with role %s from different enterprise(expected:%s actual:%s)",
                                        order.getId(), order.getStatus(), currentRole, order.getEnterpriseId(), getCurrentUser().getEnterpriseId()));
                    }
                    break;
                default:
                    break;
            }
        } catch (IllegalArgumentException e) {
            throw new ForbiddenException(ErrorCode.ORDER_ACCESS_DENIED,
                    String.format("unsupported role %s", getCurrentUser().getRole()));
        }
    }

    private boolean isSameEnterprise(String currentEnterpriseId, String orderEnterpriseId) {
        return !StringUtils.isEmpty(currentEnterpriseId) && currentEnterpriseId.equals(orderEnterpriseId);
    }

    private StatusChange getStatusChangeFromNewOrder(T order) {
        return new StatusChange(order.getOldStatus(), order.getStatus());
    }

    private StatusChange getStatusChangeFromCurrentOrder(T order, OrderStatus.Type cancelled) {
        return new StatusChange(order.getStatus(), OrderStatus.of(cancelled));
    }

    @Override
    @Transactional
    public T cancel(String orderId, CancelReason cancelReason) {
        T order = get(orderId);
        validateStatusTransition(order, getStatusChangeFromCurrentOrder(order, OrderStatus.Type.Cancelled));
        order.setStatus(OrderStatus.of(OrderStatus.Type.Cancelled));
        order.setCancelReason(cancelReason);
        return onCancel(order, cancelReason);
    }

    @Override
    @Transactional
    public T pay(String orderId, String paymentRequestIds) {
        // TODO - check transition map
        T order = get(orderId);
        validateStatusTransition(order, getStatusChangeFromCurrentOrder(order, OrderStatus.Type.Paid));
        order.setStatus(OrderStatus.of(OrderStatus.Type.Paid));
        return onPaid(order, paymentRequestIds);
    }

    @Override
    public final Optional<T> delete(String orderId) {
        Optional<T> order = onDelete(orderId);
        return order;
    }

    // default delete behaviour is mark deleted column to true
    // derived class can override this default action
    protected Optional<T> onDelete(String orderId) {
        return find(orderId).map(order -> update((T) order.markDeleted()));
    }


    protected abstract Optional<T> findOrder(String orderId);

    protected abstract List<T> onFindAll(Set<String> orderIds);

    protected abstract T onPaid(T order, String paymentRequestIds);

    protected abstract T onCancel(T order, CancelReason cancelReason);

    protected abstract T onUpdate(T order, StatusChange statusChange);

    protected abstract T createOrder(T order);
}
