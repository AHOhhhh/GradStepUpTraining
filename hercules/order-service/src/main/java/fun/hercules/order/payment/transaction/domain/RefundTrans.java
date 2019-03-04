package fun.hercules.order.payment.transaction.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fun.hercules.order.order.common.serialize.CurrencyCodeToStringSerializer;
import fun.hercules.order.order.platform.exports.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
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
public class RefundTrans extends EntityBase {
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name = "id", strategy = "fun.hercules.order.payment.transaction.component.RefundTransactionIdGenerator")
    private String id;

    private String paymentId;

    private BigDecimal payAmount;

    private String payCustId;

    private String payCustName;

    @OneToOne
    @JoinColumn(name = "currency_code_id")
    @JsonSerialize(using = CurrencyCodeToStringSerializer.class)
    private CurrencyCode currencyCode;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PayStatus payStatus = PayStatus.Success;

    @Builder.Default
    private Instant paidTime = Instant.now();

    @Column(columnDefinition = "TEXT")
    private String comment;
}
