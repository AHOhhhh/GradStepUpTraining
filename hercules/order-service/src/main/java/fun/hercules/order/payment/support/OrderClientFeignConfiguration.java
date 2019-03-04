package fun.hercules.order.payment.support;

import feign.FeignException;
import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import fun.hercules.order.order.common.signature.jwt.JwtTokenInterceptor;
import fun.hercules.order.order.common.signature.jwt.PlatformServiceUserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static feign.FeignException.errorStatus;

@Configuration
public class OrderClientFeignConfiguration {

    private final PlatformServiceUserToken platformServiceUserToken;

    @Autowired
    public OrderClientFeignConfiguration(PlatformServiceUserToken platformServiceUserToken) {
        this.platformServiceUserToken = platformServiceUserToken;
    }

    @Bean
    Request.Options feignOptions() {
        return new Request.Options(2000, 2000);
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new JwtTokenInterceptor(platformServiceUserToken);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            FeignException exception = errorStatus(methodKey, response);
            if (response.status() < 200 || response.status() > 300) {
                return new RetryableException(exception.getMessage(), exception, null);
            }
            return FeignException.errorStatus(methodKey, response);
        };
    }
}
