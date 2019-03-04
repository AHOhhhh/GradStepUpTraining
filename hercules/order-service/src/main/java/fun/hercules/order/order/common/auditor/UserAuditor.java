package fun.hercules.order.order.common.auditor;

import fun.hercules.order.order.platform.user.CurrentUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserAuditor implements AuditorAware<String> {

    private CurrentUser currentUser;

    public UserAuditor(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(currentUser.getUserId());
    }
}