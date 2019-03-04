package fun.hercules.order.order.common.utils;


import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

public class CustomFeignBuilder {
    private final Feign.Builder builder;

    public CustomFeignBuilder(Feign.Builder builder) {
        this.builder = builder;
    }

    public <T> T target(Class<T> apiType, String url) {
        return builder.target(apiType, url);
    }

    @Configuration
    @Import(FeignClientsConfiguration.class)
    public static class CustomFeignBuilderConfiguration {
        @Bean
        CustomFeignBuilder customFeignBuilder(Encoder encoder, Decoder decoder) {
            return new CustomFeignBuilder(Feign.builder()
                    .encoder(encoder)
                    .decoder(decoder));
        }
    }
}
