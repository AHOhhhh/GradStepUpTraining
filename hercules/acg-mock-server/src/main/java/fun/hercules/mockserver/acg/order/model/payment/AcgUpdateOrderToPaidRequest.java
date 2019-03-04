package fun.hercules.mockserver.acg.order.model.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fun.hercules.mockserver.acg.utils.DateTimeConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AcgUpdateOrderToPaidRequest {
    private Transactions transactions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Transactions {
        private String paymentTransaction;

        @JsonFormat(pattern = DateTimeConstants.ISO_DATETIME_WITHOUT_SECONDS, timezone = DateTimeConstants.TIME_ZONE)
        private Instant payTime;

        private float amount;

        private String currency;

        private String payMethod;
    }
}

