package fun.hercules.user.utils;

import org.springframework.util.StringUtils;

import java.util.stream.IntStream;

/**
 * 敏感数据加密器
 */
public class SensitiveDataEncoder {

    private static final char ENCODE_CHARACTER = '*';

    private static final int ENCODE_RANGE = 1;

    public static String encode(String value) {
        if (StringUtils.isEmpty(value)) {
            return value;
        } else if (value.length() <= ENCODE_RANGE * 2) {
            return generate('*', value.length());
        } else {
            return String.format("%s%s%s",
                    value.substring(0, ENCODE_RANGE),
                    generate(ENCODE_CHARACTER, value.length() - ENCODE_RANGE * 2),
                    value.substring(value.length() - ENCODE_RANGE));
        }
    }

    private static String generate(char character, int count) {
        return IntStream.range(0, count).mapToObj(value -> String.valueOf(character))
                .reduce("", String::concat);
    }
}
