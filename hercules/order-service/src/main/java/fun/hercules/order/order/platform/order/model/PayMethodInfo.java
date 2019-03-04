package fun.hercules.order.order.platform.order.model;

import fun.hercules.order.order.configurations.PaymentAccount;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PayMethodInfo {
    private PayMethod payMethod;
    private PaymentAccount paymentAccount;
}
