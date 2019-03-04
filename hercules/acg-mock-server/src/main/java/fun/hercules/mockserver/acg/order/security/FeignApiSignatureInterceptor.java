package fun.hercules.mockserver.acg.order.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FeignApiSignatureInterceptor implements RequestInterceptor {

    @Autowired
    private PlatformServiceUserToken platformServiceUserToken;

    @Override
    public void apply(RequestTemplate template) {
        template.header(HttpHeaders.AUTHORIZATION, platformServiceUserToken.getToken());
    }
}
