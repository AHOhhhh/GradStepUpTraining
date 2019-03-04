package fun.hercules.order.order.platform.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayMethodConfigDTO {
    private String enterpriseId;
    private String orderType;
    private List<PayMethodConfigItemDTO> payMethods;
}