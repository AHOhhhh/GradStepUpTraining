package fun.hercules.order.order.platform.order.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fun.hercules.order.order.platform.exports.EntityBase;
import fun.hercules.order.order.platform.exports.PayableBusinessOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.Optional;
import java.util.function.Supplier;

@NoArgsConstructor
@Data
@Entity
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@SQLDelete(sql = "UPDATE payment_request SET deleted = true WHERE id = ? AND version = ?")
public class PaymentRequest extends EntityBase {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(name = "order_id")
    private String orderId;

    @OneToOne
    @JoinColumn(name = "order_type")
    private OrderType orderType;

    @NonNull
    @Enumerated
    private PaymentStatus paymentStatus;

    @Column(name = "amount")
    private BigDecimal amount;

    private Instant paidTime;

    @NotNull
    private Currency currency;

    public PaymentRequest(PayableBusinessOrder businessOrder) {
        this(businessOrder, () -> Instant.now());
    }

    public PaymentRequest(PayableBusinessOrder businessOrder, Supplier<Instant> instantSupplier) {
        orderId = businessOrder.getId();
        orderType = OrderType.of(businessOrder.getOrderType());
        amount = businessOrder.getTotalPrice() == null ? BigDecimal.valueOf(0) : businessOrder.getTotalPrice();
        paidTime = instantSupplier.get();
        currency = Optional.ofNullable(businessOrder.getCurrency()).orElse(Currency.getInstance("CNY"));
        paymentStatus = PaymentStatus.PayInProcess;
    }
}
