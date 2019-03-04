package fun.hercules.order.order.configurations;

import fun.hercules.order.order.platform.order.model.PayMethod;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "payment-account")
@Data
public class OrderPaymentAccount {
    private List<PaymentAccount> online;
    private List<PaymentAccount> offline;
    private List<PaymentAccount> deferment;

    public PaymentAccount getPaymentAccount(String orderType, PayMethod payMethod) {
        List<PaymentAccount> paymentAccounts;
        if (payMethod.equals(PayMethod.ONLINE)) {
            paymentAccounts = online;
        } else if (payMethod.equals(PayMethod.OFFLINE)) {
            paymentAccounts = offline;
        } else {
            paymentAccounts = new ArrayList<>();
        }

        return paymentAccounts.stream()
                .filter(paymentAccount -> paymentAccount.getOrderType().equalsIgnoreCase(orderType))
                .findFirst()
                .orElse(null);
    }
}
