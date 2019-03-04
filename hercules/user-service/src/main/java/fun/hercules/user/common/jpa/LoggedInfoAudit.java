package fun.hercules.user.common.jpa;

import fun.hercules.user.user.security.CurrentUserContext;
import fun.hercules.user.user.security.CurrentUserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;

@Slf4j
public class LoggedInfoAudit implements AuditorAware<String> {

    private final CurrentUserContext userContext;

    public LoggedInfoAudit(CurrentUserContext userContext) {
        this.userContext = userContext;
    }

    @Override
    public String getCurrentAuditor() {
        return userContext.getUser().getId();
    }
}
