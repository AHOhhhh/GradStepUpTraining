package fun.hercules.order.order.platform.order.validators;

import fun.hercules.order.order.common.errors.ErrorCode;
import fun.hercules.order.order.common.exceptions.ForbiddenException;
import fun.hercules.order.order.platform.user.CurrentUser;
import fun.hercules.order.order.platform.user.Privilege;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ValidatorForUserAccess {

    public static final String WMS = "wms";
    public static final String ACG = "acg";

    public void validateAccess(String type, CurrentUser user) {
        switch (type.toLowerCase()) {
            case WMS:
                ensureAccess(WMS, user, Privilege.of(Privilege.Type.WmsOrderAccessPrivilege));
                break;
            case ACG:
                ensureAccess(ACG, user, Privilege.of(Privilege.Type.AcgOrderAccessPrivilege));
                break;
            default:
                break;
        }
    }

    public void ensureAccess(String type, CurrentUser user, Privilege orderPrivilege) {
        Set<Privilege> privileges = user.getPrivileges();
        if (!(privileges.contains(orderPrivilege) || privileges.contains(Privilege.of(Privilege.Type.AllPrivileges)))) {
            throw new ForbiddenException(ErrorCode.ORDER_ACCESS_DENIED,
                    String.format("user doesn't have access privilege to %s order", type));
        }
    }

}
