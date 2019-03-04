package fun.hercules.order.order.business.acg.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLogisticStatusRequest {
    private List<LogisticsStatus> logisticsStatus;
}
