package fun.hercules.webapi;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringBootApplication
@EnableFeignClients
public class WebAPIGatewayApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(WebAPIGatewayApplication.class)
                .run(args);
    }
}
