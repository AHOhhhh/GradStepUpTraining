package fun.hercules.order.order.platform.exports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fun.hercules.order.order.platform.order.model.CancelReason;
import fun.hercules.order.order.platform.order.model.OperationLog;
import fun.hercules.order.order.platform.order.model.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EqualsAndHashCode(callSuper = true)
@Where(clause = EntityBase.SKIP_DELETED_CLAUSE)
public abstract class BusinessOrderBase extends EntityBase implements BusinessOrder {
    @Id
    @GeneratedValue(generator = "order_id")
    @GenericGenerator(name = "order_id", strategy = "fun.hercules.order.order.platform.exports.OrderIdGenerator")
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    private String userId;

    @Transient
    private String userName;

    @JsonIgnore
    private String userRole;

    private String enterpriseId;

    @Transient
    private String enterpriseName;

    @Mutable
    @OneToOne
    @JoinColumn(name = "cancel_reason")
    private CancelReason cancelReason;

    @Mutable
    @OneToOne
    @JoinColumn(name = "status")
    private OrderStatus status;

    @NotNull
    @Size(max = 36)
    private String vendor;

    @JsonIgnore
    @Transient
    private OrderStatus oldStatus;


    private Boolean refundStatus;

    // order changes is in operator log, order change record is only used for db audit
//    @OneToMany
//    @JoinColumn(name = "orderId")
//    @OrderBy("createdAt DESC")
//    private List<OrderChangeRecord> orderChangeRecords;

    @OneToMany
    @JoinColumn(name = "orderId")
    @OrderBy("createdAt DESC")
    private List<OperationLog> operationLogs;

    @PostLoad
    @PostPersist
    @PostUpdate
    public void saveOldStatus() {
        setOldStatus(getStatus());
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

    @Override
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Instant getUpdatedAt() {
        return super.getUpdatedAt();
    }
}
