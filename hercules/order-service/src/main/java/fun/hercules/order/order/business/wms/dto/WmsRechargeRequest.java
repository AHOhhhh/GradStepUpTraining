package fun.hercules.order.order.business.wms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import fun.hercules.order.order.business.wms.domain.WmsOrder;
import fun.hercules.order.order.common.constants.DateTimeConstants;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

@Data
@ToString
public class WmsRechargeRequest {

    String orderId;

    String subscriptionId;

    Integer productId;

    @JsonFormat(pattern = DateTimeConstants.ISO_DATETIME_WITH_HOUR_OFFSET, timezone = DateTimeConstants.TIME_ZONE)
    Instant payTime;

    BigDecimal amount;

    Currency currency;

    String transaction;

    public WmsRechargeRequest(WmsOrder order, Integer productId, String paymentRequestIds) {
        orderId = order.getId();
        subscriptionId = order.getSubscriptionId();
        this.productId = productId;
        // TODO - fill this
        payTime = Instant.now();
        amount = order.getApprovedPrice();
        currency = order.getCurrency();
        // TODO - fill this
        transaction = paymentRequestIds;
    }
}
