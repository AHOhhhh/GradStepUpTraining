package fun.hercules.order.payment.transaction.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import fun.hercules.order.order.common.constants.DateTimeConstants;
import fun.hercules.order.payment.transaction.domain.PayStatus;
import fun.hercules.order.payment.transaction.domain.PaymentMethod;
import fun.hercules.order.payment.transaction.domain.TransactionType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class OfflineTransactionInfo extends TransactionInfo {
    private String bankTransactionNumber;
    @JsonFormat(pattern = DateTimeConstants.ISO_DATE, timezone = DateTimeConstants.TIME_ZONE)
    private LocalDate bankTransactionTime;
    private String bankTransactionComment;


    @Builder
    public OfflineTransactionInfo(String transactionId, String paymentId, BigDecimal payAmount, String payCustId,
                                  String payCustName, String currency, PayStatus payStatus, Instant paidTime, String comment,
                                  PaymentMethod payMethod, TransactionType type, Instant createdAt, String bankTransactionNumber,
                                  LocalDate bankTransactionTime, String bankTransactionComment) {
        super(transactionId, paymentId, payAmount, payCustId, payCustName, currency, payStatus,
                paidTime, comment, payMethod, type, createdAt);
        this.bankTransactionNumber = bankTransactionNumber;
        this.bankTransactionTime = bankTransactionTime;
        this.bankTransactionComment = bankTransactionComment;
    }
}
