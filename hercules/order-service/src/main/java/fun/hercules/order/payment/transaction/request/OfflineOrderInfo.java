package fun.hercules.order.payment.transaction.request;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class OfflineOrderInfo extends OrderInfo {

    private String depositBank;

    private String collectionAccountName;

    private String collectionAccountNumber;

    @NotNull
    private String bankTransactionNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate bankTransactionTime;

    private String bankTransactionComment;

    @Builder
    public OfflineOrderInfo(String paymentId, BigDecimal orderAmount, BigDecimal payAmount, String currencyCode, String payCustId, String payCustName,
                            String depositBank, String collectionAccountName, String collectionAccountNumber, String bankTransactionNumber, LocalDate bankTransactionTime) {
        super(paymentId, orderAmount, payAmount, currencyCode, payCustId, payCustName);
        this.depositBank = depositBank;
        this.collectionAccountName = collectionAccountName;
        this.collectionAccountNumber = collectionAccountNumber;
        this.bankTransactionNumber = bankTransactionNumber;
        this.bankTransactionTime = bankTransactionTime;
    }
}
