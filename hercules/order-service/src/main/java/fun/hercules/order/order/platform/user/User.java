package fun.hercules.order.order.platform.user;

import java.util.Set;

public interface User {
    String getUserId();

    String getEnterpriseId();

    String getRole();

    Set<Privilege> getPrivileges();
}