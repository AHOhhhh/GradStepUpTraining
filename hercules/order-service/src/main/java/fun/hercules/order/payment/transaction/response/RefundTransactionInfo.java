package fun.hercules.order.payment.transaction.response;

import fun.hercules.order.payment.transaction.domain.PayStatus;
import fun.hercules.order.payment.transaction.domain.PaymentMethod;
import fun.hercules.order.payment.transaction.domain.TransactionType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RefundTransactionInfo extends TransactionInfo {

    @Builder
    public RefundTransactionInfo(String transactionId, String paymentId, BigDecimal payAmount,
                                 String payCustId, String payCustName, String currency,
                                 PayStatus payStatus, Instant paidTime, String comment,
                                 PaymentMethod payMethod, TransactionType type, Instant createdAt) {
        super(transactionId, paymentId, payAmount, payCustId, payCustName, currency, payStatus, paidTime, comment, payMethod, type, createdAt);
    }
}
