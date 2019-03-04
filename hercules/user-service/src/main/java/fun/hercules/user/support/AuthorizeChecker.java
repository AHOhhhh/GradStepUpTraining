package fun.hercules.user.support;

import fun.hercules.user.enterprise.domain.Enterprise;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.security.CurrentUserContext;
import fun.hercules.user.enterprise.domain.Enterprise;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.security.CurrentUserContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("auth")
public class AuthorizeChecker {
    @Autowired
    private CurrentUserContext userContext;

    // 判断参数id是否与当前用户的userId相同
    public boolean isTheSameUserId(String id) {
        User user = userContext.getUser();
        return !StringUtils.isEmpty(id)
                && StringUtils.equals(user.getId(), id);
    }

    // 判断参数enterpriseId是否与当前用户的enterpriseId相同
    public boolean isTheSameEnterpriseId(String enterpriseId) {
        User user = userContext.getUser();
        return !StringUtils.isEmpty(enterpriseId)
                && StringUtils.equals(user.getEnterpriseId(), enterpriseId);
    }

    // 判断当前用户是否可以修改或提交企业信息
    public boolean isEnterpriseAdminAllowedSubmitEnterpriseInfo() {
        return userContext.getUser().getEnterpriseId() == null;
    }

    // 判断企业审核状态是否允许再次提交
    public boolean isEnterpriseAllowedReSubmit(Enterprise enterprise) {
        Enterprise.ValidationStatus validationStatus = enterprise.getValidationStatus();
        return validationStatus != null && validationStatus.equals(Enterprise.ValidationStatus.Unauthorized);
    }
}
