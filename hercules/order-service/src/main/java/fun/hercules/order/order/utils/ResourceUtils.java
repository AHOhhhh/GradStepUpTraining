package fun.hercules.order.order.utils;

import lombok.SneakyThrows;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;

public class ResourceUtils {
    @SneakyThrows
    public static String loadResource(String resourceName) {
        return StreamUtils.copyToString(ResourceUtils.class.getResourceAsStream(
                resourceName), Charset.defaultCharset()
        );
    }


}
