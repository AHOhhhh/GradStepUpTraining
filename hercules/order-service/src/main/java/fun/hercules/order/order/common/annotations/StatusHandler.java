package fun.hercules.order.order.common.annotations;

import fun.hercules.order.order.platform.order.model.OrderStatus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * Methods with this annotations will be treated as order status change handler,
 * if order changes to a new status, the corresponding status handler will be called,
 * otherwise the onUpdate method is call.
 */
@Target(value = METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StatusHandler {
    OrderStatus.Type value();
}
