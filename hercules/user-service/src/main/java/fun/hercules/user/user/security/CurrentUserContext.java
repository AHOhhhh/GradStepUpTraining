package fun.hercules.user.user.security;

import fun.hercules.user.user.domain.Role;
import fun.hercules.user.user.domain.User;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CurrentUserContext {

    @Getter
    private final User platformServiceUser;


    public CurrentUserContext(@Value("${hlp.platform.service-user.id}") String userId,
                              @Value("${hlp.platform.service-user.name}") String userName) {
        platformServiceUser = User.builder()
                .id(userId)
                .username(userName)
                // platform service user is not available for login
                .password(UUID.randomUUID().toString())
                .fullname("Platform Service User")
                .role(Role.of(Role.Type.PlatformService))
                .telephone("N/A")
                .cellphone("N/A")
                .email("PlatformServiceUser@not.available")
                .build();
    }

    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal != null && principal instanceof User) {
                return (User) principal;
            }
        }
        return platformServiceUser;
    }

}
