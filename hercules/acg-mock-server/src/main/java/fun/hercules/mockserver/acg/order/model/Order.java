package fun.hercules.mockserver.acg.order.model;

import com.google.common.collect.ImmutableList;
import fun.hercules.mockserver.acg.order.model.logisticstatus.LogisticsStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    String id;

    String name;

    String delegateOrderId;

    public void setStatus(Status status) {
        timestamp = Instant.now();
        this.status = status;
    }

    Status status;

    @Builder.Default
    Instant timestamp = Instant.now();

    @Builder.Default
    int transportIndicator = 0;

    private ImmutableList<LogisticsStatus> createLogisticStatusList() {
        return ImmutableList.of(
                new LogisticsStatus("您的订单在北京首都机场分拣完成,准备发往[博鳌],派送航班HU13212,航班时间2017-12-01",
                        Instant.now().minus(2, ChronoUnit.DAYS), "闫雨"),
                new LogisticsStatus("订单已送达[北四环货运站]",
                        Instant.now().minus(1, ChronoUnit.DAYS), "王飞"),
                new LogisticsStatus("您的订单已电子签收,欢迎下次光临.",
                        Instant.now().minus(5, ChronoUnit.HOURS), "李环")
        );
    }

    public Optional<LogisticsStatus> getLogisticsStatus() {
        if (transportIndicator >= createLogisticStatusList().size()) {
            return Optional.empty();
        }
        return Optional.ofNullable(createLogisticStatusList().get(transportIndicator));
    }

    // return value indicates whether order is delivered or not
    public boolean updateTransportIndicator() {
        transportIndicator += 1;
        return transportIndicator >= createLogisticStatusList().size();
    }


    public enum Status {
        CREATED,
        BOOKED,
        PAYED,
        TRANSPORTING,
        DELIVERED,
        CLOSED
    }
}
