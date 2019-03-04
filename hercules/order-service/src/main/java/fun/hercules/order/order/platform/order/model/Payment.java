package fun.hercules.order.order.platform.order.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fun.hercules.order.order.platform.exports.EntityBase;
import fun.hercules.order.order.platform.exports.Mutable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Where(clause = EntityBase.SKIP_DELETED_CLAUSE)
@SQLDelete(sql = "UPDATE payment SET deleted = true WHERE id = ? AND version = ?")
public class Payment extends EntityBase {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    @Mutable
    @Builder.Default
    private Boolean payRepeated = false;

    @Column(name = "amount")
    private BigDecimal amount;

    @NonNull
    @Enumerated
    private PaymentStatus status;

    private Instant paidTime;

    @NotNull
    private Currency currency;

    @NotNull
    private String payChannel;

    @NotNull
    private String payMethod;

    private String payUserId;

    @OneToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "payment_requests", joinColumns = @JoinColumn(table = "payment", name = "payment_id"),
            inverseJoinColumns = @JoinColumn(name = "request_id"))
    private List<PaymentRequest> paymentRequests;
}
