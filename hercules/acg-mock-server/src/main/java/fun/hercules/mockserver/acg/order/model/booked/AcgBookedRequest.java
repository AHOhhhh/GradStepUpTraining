package fun.hercules.mockserver.acg.order.model.booked;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@ToString
public class AcgBookedRequest {
    @NotNull
    private String message;
}
