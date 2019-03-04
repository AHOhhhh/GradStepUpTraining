package fun.hercules.order.order.common.signature.jwt;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

@Slf4j
public class JwtTokenInterceptor implements RequestInterceptor {
    private final PlatformServiceUserToken platformServiceUserToken;

    public JwtTokenInterceptor(PlatformServiceUserToken platformServiceUserToken) {
        this.platformServiceUserToken = platformServiceUserToken;
    }

    @Override
    public void apply(RequestTemplate template) {
        if (template.url().startsWith("/oauth2/authorize")) {
            template.header(HttpHeaders.AUTHORIZATION, platformServiceUserToken.getCurrentUserToken());
        } else {
            log.debug("create token for user-service with user {}", platformServiceUserToken.getUserId());
            template.header(HttpHeaders.AUTHORIZATION, platformServiceUserToken.getToken());
        }
    }
}
