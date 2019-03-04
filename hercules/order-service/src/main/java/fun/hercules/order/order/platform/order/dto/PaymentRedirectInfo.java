package fun.hercules.order.order.platform.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRedirectInfo {
    private String payChannel;
    private String redirectUrl;
    private String paymentId;
    private Map<String, Object> redirectBody;
}
