package fun.hercules.order.order.platform.user;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

@Component
public class CurrentUser implements User {

    @Getter
    private final User platformServiceUser;


    public CurrentUser(@Value("${hlp.platform.service-user.id}") String userId,
                       @Value("${hlp.platform.service-user.name}") String userName) {
        platformServiceUser = new UserImpl(userId,  null, Role.EnterpriseUser.toString(), Collections.EMPTY_SET);
    }

    private User getCurrentUser() {
        try {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (NullPointerException ex) {
            return platformServiceUser;
        }
    }


    @Override
    public String getUserId() {
        return getCurrentUser().getUserId();
    }


    @Override
    public String getEnterpriseId() {
        return getCurrentUser().getEnterpriseId();
    }

    @Override
    public String getRole() {
        return getCurrentUser().getRole();
    }

    @Override
    public Set<Privilege> getPrivileges() {
        return getCurrentUser().getPrivileges();
    }
}
