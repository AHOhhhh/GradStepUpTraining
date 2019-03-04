package fun.hercules.order.order.business.acg.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import fun.hercules.order.order.common.constants.DateTimeConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogisticsStatus {

    private String logisticsStatusInfo;

    @JsonFormat(pattern = DateTimeConstants.ISO_DATETIME_WITH_ZONE_INFO, timezone = DateTimeConstants.TIME_ZONE)
    private Instant updateInfoTime;

    private String updateInfoUserName;
}
