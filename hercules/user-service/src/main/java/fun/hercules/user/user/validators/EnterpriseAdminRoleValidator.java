package fun.hercules.user.user.validators;


import fun.hercules.user.user.domain.User;
import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.user.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;

import static fun.hercules.user.common.errors.ErrorCode.MISSING_EMAIL;
import static fun.hercules.user.common.errors.ErrorCode.MISSING_FULL_NAME;

public class EnterpriseAdminRoleValidator extends RoleValidator {

    @Override
    public void validate(User user, Errors errors) {
        super.commonValidate(user, errors);
        if (StringUtils.isEmpty(user.getFullname())) {
            rejectValue(errors, "fullname", ErrorCode.MISSING_FULL_NAME);
        }

        if (StringUtils.isEmpty(user.getEmail())) {
            rejectValue(errors, "email", ErrorCode.MISSING_EMAIL);
        }
    }
}
