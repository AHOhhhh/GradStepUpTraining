package fun.hercules.order.order.business.acg.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class PriceResponse {

    private BigDecimal pickUpFee;

    private BigDecimal dropOffFee;

    private BigDecimal airlineFee;

    private BigDecimal total;

    public PriceResponse(BigDecimal pickUpFee, BigDecimal airlineFee, BigDecimal dropOffFee) {
        this.pickUpFee = pickUpFee;
        this.airlineFee = airlineFee;
        this.dropOffFee = dropOffFee;
        this.total = pickUpFee.add(airlineFee).add(dropOffFee);
    }
}
