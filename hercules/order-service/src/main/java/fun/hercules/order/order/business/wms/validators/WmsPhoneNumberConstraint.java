package fun.hercules.order.order.business.wms.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = WmsPhoneNumberValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WmsPhoneNumberConstraint {
    String message() default "Missing phone number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
