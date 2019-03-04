package fun.hercules.order.order.common.signature;

import feign.RequestInterceptor;
import fun.hercules.order.order.common.signature.jwt.JwtPaymentInterceptor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
public class FeignPaymentConfig {

    @Bean
    public RequestInterceptor requestInterceptor(JwtPaymentInterceptor interceptor) {
        return interceptor;
    }
}
