package fun.hercules.order.order.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;

import static fun.hercules.order.order.utils.ResourceUtils.loadResource;

public class JsonUtils {
    @Getter
    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
        mapper.findAndRegisterModules();
    }

    public static <T> T unmarshal(String value, TypeReference<T> typeRef) {
        try {
            return mapper.readValue(value, typeRef);
        } catch (IOException e) {
            throw new JsonParseException(e);
        }
    }

    public static <T> T unmarshal(String value, Class<T> type) {
        try {
            return mapper.readValue(value, type);
        } catch (IOException e) {
            throw new JsonParseException(e);
        }
    }

    public static <T> T unmarshal(InputStream value, TypeReference<T> typeRef) {
        try {
            return mapper.readValue(value, typeRef);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String marshal(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String loadFixture(String resourceName) {
        return loadResource(String.format("/fixtures/%s", resourceName));
    }

    public static Object loadJsonFixture(String resourceName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(loadResource(String.format("/fixtures/%s", resourceName)), new TypeReference<List<CountryInfo>>(){});
    }

}