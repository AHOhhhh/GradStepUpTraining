package fun.hercules.order.order.business.wms.validators;

import fun.hercules.order.order.common.dto.Contact;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WmsPhoneNumberValidator implements ConstraintValidator<WmsPhoneNumberConstraint, Contact> {
    @Override
    public void initialize(WmsPhoneNumberConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(Contact contact, ConstraintValidatorContext context) {
        return !StringUtils.isEmpty(contact.getCellphone());
    }
}
