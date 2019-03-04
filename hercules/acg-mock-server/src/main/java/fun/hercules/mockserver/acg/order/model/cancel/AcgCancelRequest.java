package fun.hercules.mockserver.acg.order.model.cancel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AcgCancelRequest {
    @NotNull
    private String message;
}
