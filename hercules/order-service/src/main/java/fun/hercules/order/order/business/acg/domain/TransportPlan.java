package fun.hercules.order.order.business.acg.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import fun.hercules.order.order.business.acg.dto.plan.AcgPlanRequest;
import fun.hercules.order.order.common.constants.DateTimeConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Table(name = "transport_plan")
@NoArgsConstructor
@Data
@AllArgsConstructor
@ToString
public class TransportPlan {

    public TransportPlan(AcgPlanRequest acgPlanRequest) {
        this.scheduledPickupTime = acgPlanRequest.getScheduledPickupTime();
        this.pickupTelephone = acgPlanRequest.getPickupTelephone();
        this.scheduledFlight = acgPlanRequest.getScheduledFlight();
        this.scheduledTakeOffTime = acgPlanRequest.getScheduledTakeOffTime();
        this.scheduledLandingTime = acgPlanRequest.getScheduledLandingTime();
        this.expectedDeliveryTime = acgPlanRequest.getExpectedDeliveryTime();
        this.deliveryTelephone = acgPlanRequest.getDeliveryTelephone();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = AcgOrder.class)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private AcgOrder acgOrder;

    @JsonFormat(pattern = DateTimeConstants.ISO_DATETIME_WITH_ZONE_INFO, timezone = DateTimeConstants.TIME_ZONE)
    @Column(name = "scheduled_pickup_time")
    private Instant scheduledPickupTime;

    @Column(name = "pickup_telephone")
    private String pickupTelephone;

    @Column(name = "scheduled_flight")
    private String scheduledFlight;

    @JsonFormat(pattern = DateTimeConstants.ISO_DATETIME_WITH_ZONE_INFO, timezone = DateTimeConstants.TIME_ZONE)
    @Column(name = "scheduled_take_off_time")
    private Instant scheduledTakeOffTime;

    @JsonFormat(pattern = DateTimeConstants.ISO_DATETIME_WITH_ZONE_INFO, timezone = DateTimeConstants.TIME_ZONE)
    @Column(name = "scheduled_landing_time")
    private Instant scheduledLandingTime;

    @JsonFormat(pattern = DateTimeConstants.ISO_DATETIME_WITH_ZONE_INFO, timezone = DateTimeConstants.TIME_ZONE)
    @Column(name = "expected_delivery_time")
    private Instant expectedDeliveryTime;

    @Column(name = "delivery_telephone")
    private String deliveryTelephone;
}
