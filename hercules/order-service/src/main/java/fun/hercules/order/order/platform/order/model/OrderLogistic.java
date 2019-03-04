package fun.hercules.order.order.platform.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class OrderLogistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;
    private String logisticsStatusInfo;
    private Instant updateInfoTime;
    private String updateInfoUserName;
    @JsonIgnore
    private String orderId;

    public OrderLogistic(String logisticsStatusInfo, Instant updateInfoTime, String updateInfoUserName, String orderId) {
        this.logisticsStatusInfo = logisticsStatusInfo;
        this.updateInfoTime = updateInfoTime;
        this.updateInfoUserName = updateInfoUserName;
        this.orderId = orderId;
    }
}
