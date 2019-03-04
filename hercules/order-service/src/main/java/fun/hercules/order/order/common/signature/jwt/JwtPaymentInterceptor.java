package fun.hercules.order.order.common.signature.jwt;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class JwtPaymentInterceptor implements RequestInterceptor {

    private final PlatformServiceUserToken platformServiceUserToken;

    public JwtPaymentInterceptor(PlatformServiceUserToken platformServiceUserToken) {
        this.platformServiceUserToken = platformServiceUserToken;
    }

    @Override
    public void apply(RequestTemplate template) {
        template.header(HttpHeaders.AUTHORIZATION, platformServiceUserToken.getToken());
    }
}
