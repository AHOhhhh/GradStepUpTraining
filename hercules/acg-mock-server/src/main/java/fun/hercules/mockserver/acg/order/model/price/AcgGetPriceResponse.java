package fun.hercules.mockserver.acg.order.model.price;

import fun.hercules.mockserver.acg.order.model.AcgResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AcgGetPriceResponse extends AcgResponse {
    private BigDecimal pickUpPrice;

    private BigDecimal dropOffPrice;

    // rmb per kg
    private BigDecimal unitPriceByWeight;

    // rmb per cubic meters
    private BigDecimal unitPriceByVolume;
}
