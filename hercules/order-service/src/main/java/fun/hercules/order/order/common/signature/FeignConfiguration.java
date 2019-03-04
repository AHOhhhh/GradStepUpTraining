package fun.hercules.order.order.common.signature;

import com.netflix.hystrix.HystrixCommand;
import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.Target;
import feign.hystrix.HystrixFeign;
import fun.hercules.order.order.common.signature.annotations.JwtSignature;
import fun.hercules.order.order.common.signature.jwt.JwtTokenInterceptor;
import fun.hercules.order.order.common.signature.jwt.PlatformServiceUserToken;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Configuration
@EnableFeignClients
public class FeignConfiguration {


    private final PlatformServiceUserToken platformServiceUserToken;

    public FeignConfiguration(PlatformServiceUserToken platformServiceUserToken) {
        this.platformServiceUserToken = platformServiceUserToken;
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    @Scope("prototype")
    @Primary
    public Feign.Builder feignBuilder() {
        return new AutoSignatureFeignBuilder(platformServiceUserToken).retryer(Retryer.NEVER_RETRY);
    }

    @Configuration
    protected static class HystrixFeignConfiguration {
        @Bean
        @Scope("prototype")
        @ConditionalOnMissingBean
        @ConditionalOnProperty(name = "feign.hystrix.enabled")
        public Feign.Builder feignHystrixBuilder() {
            return HystrixFeign.builder();
        }
    }

    private static class AutoSignatureFeignBuilder extends Feign.Builder {

        private final PlatformServiceUserToken platformServiceUserToken;

        public AutoSignatureFeignBuilder(PlatformServiceUserToken platformServiceUserToken) {
            this.platformServiceUserToken = platformServiceUserToken;
        }

        @Override
        public <T> T target(Target<T> target) {
            if (target.type().isAnnotationPresent(JwtSignature.class)) {
                super.requestInterceptor(new JwtTokenInterceptor(platformServiceUserToken));
            }
            return super.target(target);
        }
    }
}
