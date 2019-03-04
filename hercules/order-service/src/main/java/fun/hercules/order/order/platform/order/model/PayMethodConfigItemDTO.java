package fun.hercules.order.order.platform.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayMethodConfigItemDTO {
    private String name;
    private boolean editable;
    private boolean enabled;
}
