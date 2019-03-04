package fun.hercules.order.payment.transaction.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class RefundOrderInfo {

    private String paymentId;

    private BigDecimal payAmount;

    private String currencyCode;

    private String payCustId;

    private String payCustName;

    private String comment;
}
