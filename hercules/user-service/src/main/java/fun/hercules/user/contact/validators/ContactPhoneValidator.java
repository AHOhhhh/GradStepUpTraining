package fun.hercules.user.contact.validators;

import fun.hercules.user.contact.domain.Contact;
import fun.hercules.user.contact.domain.Contact;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContactPhoneValidator implements ConstraintValidator<ContactPhoneConstraint, Contact> {
    @Override
    public void initialize(ContactPhoneConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(Contact contact, ConstraintValidatorContext context) {
        return contact.getTelephone() != null || contact.getCellphone() != null;
    }
}
