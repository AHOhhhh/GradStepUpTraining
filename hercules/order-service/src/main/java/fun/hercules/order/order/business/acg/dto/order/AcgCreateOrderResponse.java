package fun.hercules.order.order.business.acg.dto.order;

import fun.hercules.order.order.business.acg.dto.AcgResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AcgCreateOrderResponse extends AcgResponse {
    String delegateOrderId;
}
