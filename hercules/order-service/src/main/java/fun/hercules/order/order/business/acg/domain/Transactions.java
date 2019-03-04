package fun.hercules.order.order.business.acg.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import fun.hercules.order.order.common.constants.DateTimeConstants;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class Transactions {
    private String paymentTransaction;

    @JsonFormat(pattern = DateTimeConstants.ISO_DATETIME_WITHOUT_SECONDS, timezone = DateTimeConstants.TIME_ZONE)
    private Instant payTime;

    private float payment;

    private String currency;

    private String payMethod;
}
