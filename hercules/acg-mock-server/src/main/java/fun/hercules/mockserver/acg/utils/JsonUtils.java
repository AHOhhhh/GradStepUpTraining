package fun.hercules.mockserver.acg.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;

import java.io.IOException;
import java.io.UncheckedIOException;

public class JsonUtils {
    @Getter
    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.findAndRegisterModules();
    }

    public static <T> T unmarshal(String value, TypeReference<T> typeRef) {
        try {
            return mapper.readValue(value, typeRef);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T unmarshal(String value, Class<T> type) {
        try {
            return mapper.readValue(value, type);
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
}