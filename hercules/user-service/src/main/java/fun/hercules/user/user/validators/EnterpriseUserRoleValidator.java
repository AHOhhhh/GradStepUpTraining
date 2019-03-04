package fun.hercules.user.user.validators;


import fun.hercules.user.user.domain.User;
import org.springframework.validation.Errors;

public class EnterpriseUserRoleValidator extends RoleValidator {

    @Override
    public void validate(User user, Errors errors) {
        super.commonValidate(user, errors);
    }
}
