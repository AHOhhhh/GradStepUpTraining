package fun.hercules.order.payment.transaction.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfoResponse {
    private String payChannel;
    private String redirectUrl;
    private Map<String, Object> redirectBody;
}
