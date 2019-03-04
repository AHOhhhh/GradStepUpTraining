package fun.hercules.order.order.configurations;

import lombok.Data;

@Data
public class PaymentAccount {
    private String orderType;
    private String depositBank;
    private String collectionAccountName;
    private String collectionAccountNumber;
    private String payChannel;
}
