package fun.hercules.mockserver.acg.order.model.logisticstatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fun.hercules.mockserver.acg.utils.DateTimeConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogisticsStatus {

    private String logisticsStatusInfo;

    @JsonFormat(pattern = DateTimeConstants.ISO_DATETIME_WITH_ZONE_INFO, timezone = DateTimeConstants.TIME_ZONE)
    private Instant updateInfoTime;

    private String updateInfoUserName;
}