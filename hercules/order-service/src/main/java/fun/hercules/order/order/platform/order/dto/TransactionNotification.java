package fun.hercules.order.order.platform.order.dto;

import fun.hercules.order.order.platform.order.model.PayMethod;
import fun.hercules.order.order.platform.order.model.PaymentStatus;
import fun.hercules.order.order.platform.order.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionNotification {
    private String transactionId;
    private String paymentId;
    private BigDecimal payAmount;
    private TransactionType type;
    private PayMethod payMethod;
    private String payCustId;
    private String payCustName;
    private Currency currency;
    private PaymentStatus payStatus;
    private Instant paidTime;
    private String comment;
}
