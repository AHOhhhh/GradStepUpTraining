package fun.hercules.mockserver.acg.order.model.plan;

import com.fasterxml.jackson.annotation.JsonFormat;
import fun.hercules.mockserver.acg.utils.DateTimeConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AcgPlanRequest {
    @NotNull
    @JsonFormat(pattern = DateTimeConstants.ISO_DATETIME_WITH_ZONE_INFO, timezone = DateTimeConstants.TIME_ZONE)
    private Instant scheduledPickupTime;

    @NotNull
    private String pickupTelephone;

    @NotNull
    private String scheduledFlight;

    @NotNull
    private String delegateOrderId;

    @NotNull
    @JsonFormat(pattern = DateTimeConstants.ISO_DATETIME_WITH_ZONE_INFO, timezone = DateTimeConstants.TIME_ZONE)
    private Instant scheduledTakeOffTime;

    @NotNull
    @JsonFormat(pattern = DateTimeConstants.ISO_DATETIME_WITH_ZONE_INFO, timezone = DateTimeConstants.TIME_ZONE)
    private Instant scheduledLandingTime;

    @NotNull
    @JsonFormat(pattern = DateTimeConstants.ISO_DATETIME_WITH_ZONE_INFO, timezone = DateTimeConstants.TIME_ZONE)
    private Instant expectedDeliveryTime;

    @NotNull
    private String deliveryTelephone;
}
