package fun.hercules.order.order.platform.order.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fun.hercules.order.order.platform.order.model.PayChannel;
import fun.hercules.order.order.platform.order.model.PayMethod;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfflinePaymentInfo extends PaymentInfo {

    private String depositBank;
    private String collectionAccountName;
    private String collectionAccountNumber;
    private String bankTransactionNumber;
    private String bankTransactionTime; // to be checked
    private String bankTransactionComment;

    @Builder
    public OfflinePaymentInfo(String orderId, List<String> payRequestIds, BigDecimal orderAmount, BigDecimal payAmount, String currencyCode, String businessType,
                              String payCustId, String payCustName, String paymentId, PayChannel payChannel, PayMethod payMethod, String depositBank, String collectionAccountName,
                              String collectionAccountNumber, String bankTransactionNumber, String bankTransactionTime, String bankTransactionComment) {
        super(orderId, payRequestIds, orderAmount, payAmount, currencyCode, businessType, payCustId, payCustName, paymentId, payChannel, payMethod);
        this.depositBank = depositBank;
        this.collectionAccountName = collectionAccountName;
        this.collectionAccountNumber = collectionAccountNumber;
        this.bankTransactionNumber = bankTransactionNumber;
        this.bankTransactionTime = bankTransactionTime;
        this.bankTransactionComment = bankTransactionComment;
    }
}
