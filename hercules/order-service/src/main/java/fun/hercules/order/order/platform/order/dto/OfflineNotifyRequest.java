package fun.hercules.order.order.platform.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfflineNotifyRequest {
    private String transactionId;
    private boolean isConfirmed;
    private String comment;
}
