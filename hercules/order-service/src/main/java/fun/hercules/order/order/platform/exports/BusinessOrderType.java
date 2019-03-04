package fun.hercules.order.order.platform.exports;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Target(value = {TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BusinessOrderType {
    Class<? extends BusinessOrder> value();
}
