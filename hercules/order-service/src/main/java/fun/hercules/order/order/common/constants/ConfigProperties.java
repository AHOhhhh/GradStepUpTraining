package fun.hercules.order.order.common.constants;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@NoArgsConstructor
@Data
@Getter
@Component
public class ConfigProperties {

    @Getter
    private static ConfigProperties instance = new ConfigProperties(ZoneId.systemDefault().getId());

    // Asia/Shanghai
    private ZoneId timeZone;

    public ConfigProperties(@Value("${hlp.platform.time-zone}") String zoneId) {
        bindInstance();
        this.timeZone = ZoneId.of(zoneId);
    }

    private synchronized void bindInstance() {
        instance = this;
    }
}
