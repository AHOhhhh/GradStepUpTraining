package fun.hercules.order.order.platform.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fun.hercules.order.order.platform.exports.Auditable;
import fun.hercules.order.order.platform.exports.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Where(clause = EntityBase.SKIP_DELETED_CLAUSE)
@SQLDelete(sql = "UPDATE operation_log SET deleted = true WHERE id = ? AND version = ?")
@ToString(exclude = {"operator", "operatorName"})
public class OperationLog extends EntityBase implements Auditable {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    private String vendor;

    @NotNull
    private String orderId;

    @OneToOne
    @JoinColumn(name = "status")
    private OrderStatus status;

    private String operator;

    @Transient
    private String operatorName;

    @Transient
    private int index;

    private String operatorRole;

    @OneToOne
    @JoinColumn(name = "operation")
    private OperationType operation;

    public String getOperationName() {
        return operation.getDescription();
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Instant getCreatedAt() {
        return super.getCreatedAt();
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getCreatedBy() {
        return super.getCreatedBy();
    }
}
