package fun.hercules.order.order.business.acg.dto.price;

import fun.hercules.order.order.business.acg.dto.AcgResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class AcgGetPriceResponse extends AcgResponse {
    private BigDecimal pickUpPrice;

    private BigDecimal dropOffPrice;

    // rmb per kg
    private BigDecimal unitPriceByWeight;

    // rmb per cubic meters
    private BigDecimal unitPriceByVolume;
}

