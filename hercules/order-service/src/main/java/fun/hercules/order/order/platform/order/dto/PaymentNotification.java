package fun.hercules.order.order.platform.order.dto;

import fun.hercules.order.order.platform.order.model.PayMethod;
import fun.hercules.order.order.platform.order.model.PaymentStatus;
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
    private PaymentStatus status;
    private boolean payRepeated;
    private Instant paidTime;
    //person who pay the money
    private String paymentUserId;
    //operator who does the confirmation for offline payment
    private String operatorId;
    private PayMethod payMethod;
}
