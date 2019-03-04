package fun.hercules.order.order.business.acg.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import fun.hercules.order.order.common.constants.DateTimeConstants;
import fun.hercules.order.order.common.dto.Contact;
import fun.hercules.order.order.platform.exports.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@NoArgsConstructor
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Where(clause = EntityBase.SKIP_DELETED_CLAUSE)
@SQLDelete(sql = "UPDATE acg_shipping_info SET deleted = true WHERE id = ? AND version = ?")
public class AcgShippingInfo extends EntityBase {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Convert(converter = Contact.ContactConverter.class)
    @Column(columnDefinition = "TEXT")
    private Contact pickUpAddress;

    @Convert(converter = Contact.ContactConverter.class)
    @Column(columnDefinition = "TEXT")
    private Contact dropOffAddress;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = DateTimeConstants.ISO_DATETIME_WITHOUT_SECONDS,
            timezone = DateTimeConstants.TIME_ZONE)
    private Instant estimatedDeliveryTime;

    @Valid
    @NotNull
    private DepartureAirport departure;

    @Valid
    @NotNull
    private ArrivalAirport arrival;

    @PrePersist
    @PreUpdate
    public void validate() {
        if (departure != null && departure.isDelivery() && pickUpAddress == null) {
            throw new ConstraintViolationException("pick-up address when delivery enabled for departure", null);
        }
        if (arrival != null && arrival.isDelivery() && dropOffAddress == null) {
            throw new ConstraintViolationException("pick-up address when delivery enabled for arrival", null);
        }
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Embeddable
    public static class DepartureAirport {
        @Column(name = "departure_airport_id")
        @Size(min = 3, max = 3)
        private String airportId;

        @Column(name = "departure_delivery")
        @NotNull
        private boolean delivery;

        @Transient
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private String airportName;

        @Transient
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private Boolean abroad;
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Embeddable
    public static class ArrivalAirport {
        @Size(min = 3, max = 3)
        @Column(name = "arrival_airport_id")
        private String airportId;

        @Column(name = "arrival_delivery")
        @NotNull
        private boolean delivery;

        @Transient
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private String airportName;

        @Transient
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private Boolean abroad;
    }

}
