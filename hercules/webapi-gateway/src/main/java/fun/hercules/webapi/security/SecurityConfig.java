package fun.hercules.webapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import fun.hercules.webapi.client.UserClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@Configuration
@Profile(value = {"!test"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserClient userClient;

    private ObjectMapper objectMapper;

    private Boolean isVerifyCaptcha;

    public SecurityConfig(UserClient userClient,
                          ObjectMapper objectMapper,
                          @Value("${verify.captcha:true}") Boolean isVerifyCaptcha) {
        this.userClient = userClient;
        this.objectMapper = objectMapper;
        this.isVerifyCaptcha = isVerifyCaptcha;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/**/swagger-resources/configuration/ui",
                        "/swagger-ui.html/**",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/v2/api-docs",
                        "/v2/api-docs",
                        "/webapi/acg-mock/**")
                .antMatchers(HttpMethod.GET, "/webapi/token")
                .antMatchers(HttpMethod.POST, "/webapi/??crypt")
                .requestMatchers(new AcgListByIdsRequestMatcher());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/system/**").permitAll()
                .antMatchers("/webapi/captcha").permitAll()
                .antMatchers("/webapi/dictionary").permitAll()
                .antMatchers("/webapi/doc/**").permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll()
                .antMatchers("/webapi/acg-mock/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new AuthenticationFilter(userClient, objectMapper, ImmutableMap.of("verify.captcha", isVerifyCaptcha)))
                .addFilter(new PlatformAuthenticationFilter(userClient, objectMapper, ImmutableMap.of("verify.captcha", isVerifyCaptcha)))
                .addFilterAfter(new AuthorizationFilter(authenticationManager(), userClient, objectMapper), PlatformAuthenticationFilter.class)
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
