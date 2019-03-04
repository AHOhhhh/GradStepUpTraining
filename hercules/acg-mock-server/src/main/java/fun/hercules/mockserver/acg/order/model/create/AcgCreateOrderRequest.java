package fun.hercules.mockserver.acg.order.model.create;

import com.fasterxml.jackson.annotation.JsonFormat;
import fun.hercules.mockserver.acg.utils.DateTimeConstants;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel("Create Order Request")
public class AcgCreateOrderRequest {
    String orderId;

    String fromAirportCode;

    String toAirportCode;

    String goodName;

    // unit: kg
    BigDecimal goodWeight;

    // unit: Cubic centimetre
    BigDecimal goodVolume;

    @JsonFormat(pattern = DateTimeConstants.ISO_DATETIME_WITH_ZONE_INFO, timezone = DateTimeConstants.TIME_ZONE)
    Instant expectDeliverTime;
}
