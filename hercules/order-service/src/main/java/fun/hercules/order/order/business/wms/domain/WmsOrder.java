package fun.hercules.order.order.business.wms.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fun.hercules.order.order.business.wms.constants.WmsDefaultInformation;
import fun.hercules.order.order.business.wms.validators.WmsOrderConstraint;
import fun.hercules.order.order.business.wms.validators.WmsPhoneNumberConstraint;
import fun.hercules.order.order.common.constants.DateTimeConstants;
import fun.hercules.order.order.common.dto.Contact;
import fun.hercules.order.order.platform.exports.BusinessOrderBase;
import fun.hercules.order.order.platform.exports.BusinessType;
import fun.hercules.order.order.platform.exports.Mutable;
import fun.hercules.order.order.platform.exports.PayableBusinessOrder;
import fun.hercules.order.order.platform.order.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@BusinessType(value = "wms", prefix = "4")
@WmsOrderConstraint
@SQLDelete(sql = "UPDATE wms_order SET deleted = true WHERE id = ? AND version = ?")
@ToString(exclude = {"contact"})
public class WmsOrder extends BusinessOrderBase implements PayableBusinessOrder {
    @NotNull
    private String subscriptionId;

    @NotNull
    @Convert(converter = Contact.ContactConverter.class)
    @Column(columnDefinition = "TEXT")
    @WmsPhoneNumberConstraint
    private Contact contact;

    @Convert(converter = ChargingRule.ChargingRulesConverter.class)
    @Column(columnDefinition = "TEXT")
    @Mutable
    private List<ChargingRule> chargingRules;

    @Mutable
    @Column(columnDefinition = "VARCHAR(10)")
    private Currency currency;

    @Mutable
    @JsonFormat(pattern = DateTimeConstants.ISO_DATE, timezone = DateTimeConstants.TIME_ZONE)
    @Column(columnDefinition = "DATETIME")
    private LocalDate effectiveFrom;

    @Mutable
    @JsonFormat(pattern = DateTimeConstants.ISO_DATE, timezone = DateTimeConstants.TIME_ZONE)
    @Column(columnDefinition = "DATETIME")
    private LocalDate effectiveTo;

    @Mutable
    private BigDecimal approvedPrice;

    @OneToOne
    @JoinColumn(name = "type")
    private WmsOrderType type;

    @Mutable
    private String serviceIntro;

    @Mutable
    private Integer maxPrice;

    @Mutable
    private Integer minPrice;

    @Mutable
    private String enterpriseShortName;

    @Override
    public List<String> getOrderSubTypes() {
        return new ArrayList<>(Arrays.asList(type.getName()));
    }

    @Override
    public List<String> getOrderSubTypesForDisplay() {
        return new ArrayList<>(Arrays.asList(type.getDescription()));
    }

    @Override
    @JsonIgnore
    public BigDecimal getTotalPrice() {
        return approvedPrice;
    }

    // TODO - persist this filed as column after show case
    public String getServiceTelephone() {
        return WmsDefaultInformation.getInstance().getTelephone();
    }

    // for failed payment
    public void setStatus(OrderStatus status) {
        if (status.getType().equals(OrderStatus.Type.WaitForPay)) {
            if (type.equals(WmsOrderType.of(WmsOrderType.Type.Recharge))) {
                super.setStatus(OrderStatus.of(OrderStatus.Type.Submitted));
            } else {
                super.setStatus(OrderStatus.of(OrderStatus.Type.Audited));
            }
        } else {
            super.setStatus(status);
        }
    }
}


