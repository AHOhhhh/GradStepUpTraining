package fun.hercules.order.payment.transaction.response;

import fun.hercules.order.payment.transaction.domain.PayStatus;
import fun.hercules.order.payment.transaction.domain.PaymentMethod;
import fun.hercules.order.payment.transaction.domain.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionNotification {
    private String transactionId;
    private String paymentId;
    private BigDecimal payAmount;
    private TransactionType type;
    private PaymentMethod payMethod;
    private String payCustId;
    private String payCustName;
    private Currency currency;
    private PayStatus payStatus;
    private Instant paidTime;
    private String comment;
}
