package fun.hercules.order.order.utils;

import fun.hercules.order.order.common.constants.ConfigProperties;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class DateUtils {
    public static <T extends TemporalAccessor> String format(String pattern, T instant) {
        return DateTimeFormatter.ofPattern(pattern).withZone(ConfigProperties.getInstance().getTimeZone()).format(instant);
    }
}
