package fun.hercules.order.payment.transaction.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OnlineNotifyRequest {

    private String paymentId;
    private boolean sync;
}
