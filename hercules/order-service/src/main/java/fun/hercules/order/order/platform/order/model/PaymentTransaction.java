package fun.hercules.order.order.platform.order.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fun.hercules.order.order.platform.exports.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Where(clause = EntityBase.SKIP_DELETED_CLAUSE)
@SQLDelete(sql = "UPDATE payment_transaction SET deleted = true WHERE id = ? AND version = ?")
public class PaymentTransaction extends EntityBase {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    @NotNull
    private String transactionId;

    @NotNull
    private String paymentId;

    @NotNull
    private String orderId;

    @NotNull
    private String enterpriseId;

    @Transient
    private String enterpriseName;

    @NotNull
    private String vendor;

    @NotNull
    @Enumerated
    private PayMethod payMethod;

    @NonNull
    @Enumerated
    private PaymentStatus status;

    @OneToOne
    @JoinColumn(name = "order_type")
    private OrderType orderType;

    @NotNull
    @Enumerated
    private TransactionType transactionType;

    @NotNull
    private BigDecimal amount;

    private Instant paidTime;

    @NotNull
    @Column(columnDefinition = "VARCHAR(10)")
    private Currency currency;

    @Column(columnDefinition = "TEXT")
    private String comment;
}
