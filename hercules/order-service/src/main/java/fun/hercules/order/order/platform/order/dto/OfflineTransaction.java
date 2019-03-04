package fun.hercules.order.order.platform.order.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OfflineTransaction extends TransactionNotification {
    private String bankTransactionNumber;
    private String bankTransactionTime;
    private String bankTransactionComment;
}
