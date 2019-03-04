package fun.hercules.mockserver.acg.order.model.logisticstatus;

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


