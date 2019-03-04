package fun.hercules.user.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import fun.hercules.user.common.jpa.LoggedInfoAudit;
import fun.hercules.user.user.security.CurrentUserContext;
import fun.hercules.user.utils.JsonUtils;
import fun.hercules.user.user.security.CurrentUserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableJpaAuditing
public class AppConfiguration {

    @Autowired
    private CurrentUserContext userContext;

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JsonUtils.getMapper();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return new LoggedInfoAudit(userContext);
    }
}
