package fun.hercules.mockserver.acg.order.model.create;

import fun.hercules.mockserver.acg.order.model.AcgResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class AcgCreateOrderResponse extends AcgResponse {
    String delegateOrderId;

    public AcgCreateOrderResponse(String delegateOrderId) {
        super(Status.SUCCESS);
        this.delegateOrderId = delegateOrderId;
    }
}
