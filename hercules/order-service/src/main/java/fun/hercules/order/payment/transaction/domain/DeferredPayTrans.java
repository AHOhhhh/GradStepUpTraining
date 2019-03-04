package fun.hercules.order.payment.transaction.domain;


import fun.hercules.order.order.platform.exports.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(callSuper = true)
public class DeferredPayTrans extends EntityBase {
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name = "id", strategy = "fun.hercules.order.payment.transaction.component.DeferredTransactionIdGenerator")
    private String id;

    private String paymentId;

    private BigDecimal orderAmount;

    private BigDecimal payAmount;

    private String payCustId;

    private String payCustName;

    @OneToOne
    @JoinColumn(name = "currency_code_id")
    private CurrencyCode currencyCode;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PayStatus payStatus = PayStatus.PayInProcess;

    @Builder.Default
    private boolean payRepeated = false;

    private Instant paidTime;
}
