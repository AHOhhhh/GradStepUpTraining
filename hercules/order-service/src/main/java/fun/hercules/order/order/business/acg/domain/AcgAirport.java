package fun.hercules.order.order.business.acg.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fun.hercules.order.order.business.acg.validation.AirportConstraint;
import fun.hercules.order.order.platform.exports.EntityBase;
import fun.hercules.order.order.platform.exports.Mutable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@AirportConstraint
@SQLDelete(sql = "UPDATE acg_airport SET deleted = true WHERE id = ? AND version = ?")
public class AcgAirport extends EntityBase {

    @NotNull
    @Mutable
    private String city;

    @NotNull
    @Mutable
    private String name;

    @Id
    @NotNull
    @Mutable
    @Size(min = 3, max = 3)
    private String airportId;

    @NotNull
    @Mutable
    private String airportName;

    @NotNull
    @Mutable
    private Boolean pickup;

    @NotNull
    @Mutable
    private Boolean delivery;

    @NotNull
    @Mutable
    private Boolean abroad;
}
