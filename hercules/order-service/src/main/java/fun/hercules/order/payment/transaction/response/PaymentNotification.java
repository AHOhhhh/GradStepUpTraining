package fun.hercules.order.payment.transaction.response;

import fun.hercules.order.payment.transaction.domain.PayStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentNotification {

    private String paymentId;
    @Builder.Default
    private PayStatus status = PayStatus.PayInProcess;
    @Builder.Default
    private boolean payRepeated = false;
    private Instant paidTime;
    private String paymentUserId;
    private String operatorId;
}
