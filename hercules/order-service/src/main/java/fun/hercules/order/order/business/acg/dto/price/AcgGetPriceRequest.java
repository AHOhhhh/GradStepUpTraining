package fun.hercules.order.order.business.acg.dto.price;

import fun.hercules.order.order.business.acg.domain.PriceRequest;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AcgGetPriceRequest {
    private String fromAirportId;

    private String toAirportId;


    public AcgGetPriceRequest(PriceRequest request) {
        fromAirportId = request.getDeparture().getAirportId();
        toAirportId = request.getArrival().getAirportId();
    }
}
