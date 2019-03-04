package fun.hercules.order.order.platform.order.dto;

import fun.hercules.order.order.platform.order.model.CancelReason;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cancellation {
    private CancelReason cancelReason;
}
