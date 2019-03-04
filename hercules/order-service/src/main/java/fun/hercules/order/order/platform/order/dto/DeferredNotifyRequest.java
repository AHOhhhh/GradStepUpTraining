package fun.hercules.order.order.platform.order.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DeferredNotifyRequest {
    private List<String> paymentIds;
}
