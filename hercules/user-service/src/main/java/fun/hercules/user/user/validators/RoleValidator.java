package fun.hercules.user.user.validators;


import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.user.domain.User;
import org.springframework.validation.Errors;


public abstract class RoleValidator {

    private PasswordChecker passwordChecker = new PasswordChecker();


    public void commonValidate(User user, Errors errors) {
        if (user.getPassword() == null || user.getPassword().length() < 8) {
            rejectValue(errors, "password", ErrorCode.PASSWORD_LENGTH_TOO_SHORT);
        }
        if (!passwordChecker.check(user.getPassword())) {
            rejectValue(errors, "password", ErrorCode.WEAK_PASSWORD);
        }
        if (user.getCellphone() == null && user.getTelephone() == null) {
            rejectValue(errors, "cellphone", ErrorCode.MISSING_PHONE_NUMBER);
        }
    }

    public abstract void validate(User user, Errors errors);

    public void rejectValue(Errors errors, String field, ErrorCode code) {
        errors.rejectValue(field, code.toString(), code.getMessage());
    }
}
