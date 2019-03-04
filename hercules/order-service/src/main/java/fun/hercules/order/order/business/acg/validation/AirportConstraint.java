package fun.hercules.order.order.business.acg.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = AirportValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AirportConstraint {
    String message() default "AcgAirport validation failed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
