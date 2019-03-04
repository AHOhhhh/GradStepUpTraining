package fun.hercules.order.order.utils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
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

    public static String loadFixture(String resourceName) {
        return loadResource(String.format("/fixtures/%s", resourceName));
    }

    public static DocumentContext loadJsonFixture(String resourceName) {
        return JsonPath.parse(loadResource(String.format("/fixtures/%s", resourceName)));
    }
}
