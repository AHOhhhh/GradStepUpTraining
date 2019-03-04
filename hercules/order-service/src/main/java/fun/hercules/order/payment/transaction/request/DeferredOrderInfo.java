package fun.hercules.order.payment.transaction.request;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class DeferredOrderInfo extends OrderInfo {

    @Builder
    public DeferredOrderInfo(String paymentId,
                             BigDecimal orderAmount,
                             BigDecimal payAmount,
                             String currencyCode,
                             String payCustId,
                             String payCustName) {
        super(paymentId, orderAmount, payAmount, currencyCode, payCustId, payCustName);
    }
}
