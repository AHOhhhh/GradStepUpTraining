package fun.hercules.order;

import fun.hercules.order.order.common.utils.SpringContextUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
@EnableAsync
@EnableCaching
@EnableCircuitBreaker
public class OrderApplication {

    public static void main(String[] args) {
        System.setProperty("spring.security.strategy", "MODE_INHERITABLETHREADLOCAL");
        SpringContextUtil springContextUtil = new SpringContextUtil();
        ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(OrderApplication.class)
                .run(args);
        springContextUtil.setApplicationContext(applicationContext);
    }

}
