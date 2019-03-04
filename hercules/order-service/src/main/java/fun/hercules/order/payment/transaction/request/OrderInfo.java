package fun.hercules.order.payment.transaction.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {

    private String paymentId;

    private BigDecimal orderAmount;

    private BigDecimal payAmount;

    private String currencyCode;

    private String payCustId;

    private String payCustName;
}
