package fun.hercules.mockserver.acg.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AcgResponse {
    private Status status;

    public AcgResponse() {
        this.status = Status.SUCCESS;
    }

    public static AcgResponse success() {
        return new AcgResponse(Status.SUCCESS);
    }

    public static AcgResponse failure() {
        return new AcgResponse(Status.FAILURE);
    }

    public enum Status {
        SUCCESS, FAILURE
    }
}
