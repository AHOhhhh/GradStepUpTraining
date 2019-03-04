package fun.hercules.user.user.validators;

import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.common.exceptions.BadRequestException;
import fun.hercules.user.user.domain.Role;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.common.exceptions.BadRequestException;
import fun.hercules.user.user.domain.Role;
import fun.hercules.user.user.domain.User;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (user.getRole() == null) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST,
                    String.format("failed to validate user %s, the role should not be empty", user.getUsername()));
        }
        RoleValidator roleValidator = getValidator(user.getRole());

        roleValidator.validate(user, errors);
    }

    private RoleValidator getValidator(Role role) {
        switch (role.getType()) {
            case EnterpriseAdmin:
                return new EnterpriseAdminRoleValidator();
            case EnterpriseUser:
                return new EnterpriseUserRoleValidator();
            default:
                throw new BadRequestException(ErrorCode.INVALID_REQUEST,
                        String.format("invalidate role %s", role.getName()));
        }
    }
}
