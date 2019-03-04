package fun.hercules.user.util;

import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;

public class FileUtils {

    public static String loadTestSuiteResource(String filename) {
        try {
            return StreamUtils.copyToString(FileUtils.class.getResourceAsStream(
                    String.format("/fixtures/%s", filename)),
                    Charset.defaultCharset()
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
