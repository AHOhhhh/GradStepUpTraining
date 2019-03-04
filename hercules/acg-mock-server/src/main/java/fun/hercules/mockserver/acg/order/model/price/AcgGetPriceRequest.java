package fun.hercules.mockserver.acg.order.model.price;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AcgGetPriceRequest {
    private String fromAirportId;

    private String toAirportId;
}
