package fun.hercules.order.order.platform.exports;

import fun.hercules.order.order.platform.order.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusChange {
    private final OrderStatus oldStatus;

    private final OrderStatus newStatus;

    public boolean isChanged() {
        return !oldStatus.equals(newStatus);
    }
}
