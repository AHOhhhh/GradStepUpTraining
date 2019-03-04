package fun.hercules.order.order.business.acg.dto.booked;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
@AllArgsConstructor
@ToString
public class AcgBookedRequest {
    @NotNull
    private String message;
}
