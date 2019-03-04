package fun.hercules.order.order.business.acg.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fun.hercules.order.order.common.constants.DateTimeConstants;
import fun.hercules.order.order.common.dto.Contact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceRequest {

    private List<Goods> goods;

    private Airport departure;

    private Airport arrival;

    private Contact pickUpAddress;

    private Contact dropOffAddress;

    @JsonFormat(pattern = DateTimeConstants.ISO_DATETIME_WITHOUT_SECONDS, timezone = DateTimeConstants.TIME_ZONE)
    private LocalDateTime estimatedDeliveryTime;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Airport {
        private String airportId;

        private boolean delivery;
    }
}



