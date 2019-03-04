package fun.hercules.order.payment.transaction.response;


import fun.hercules.order.payment.transaction.domain.PayStatus;
import fun.hercules.order.payment.transaction.domain.PaymentMethod;
import fun.hercules.order.payment.transaction.domain.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class TransactionInfo {
    private String transactionId;
    private String paymentId;
    private BigDecimal payAmount;
    private String payCustId;
    private String payCustName;
    private String currency;
    private PayStatus payStatus;
    private Instant paidTime;
    private String comment;
    private PaymentMethod payMethod;
    private TransactionType type;
    private Instant createdAt;
}
