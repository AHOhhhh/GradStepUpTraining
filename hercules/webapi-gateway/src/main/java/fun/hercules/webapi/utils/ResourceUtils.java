package fun.hercules.webapi.utils;

import lombok.SneakyThrows;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

public class ResourceUtils {
    @SneakyThrows
    public static String loadResource(String resourceName) {
        return StreamUtils.copyToString(ResourceUtils.class.getResourceAsStream(
                resourceName), StandardCharsets.UTF_8
        );
    }


}
