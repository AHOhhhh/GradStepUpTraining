package fun.hercules.mockserver.acg.order.model.complete;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@ToString
public class AcgCompleteRequest {
    @NotNull
    private String message;
}
