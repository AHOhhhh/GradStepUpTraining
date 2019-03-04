package fun.hercules.order.order.business.acg.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import fun.hercules.order.order.common.dto.Contact;
import fun.hercules.order.order.platform.exports.BusinessOrderBase;
import fun.hercules.order.order.platform.exports.BusinessType;
import fun.hercules.order.order.platform.exports.Mutable;
import fun.hercules.order.order.platform.exports.PayableBusinessOrder;
import fun.hercules.order.order.platform.order.model.OrderLogistic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@BusinessType(value = "acg", prefix = "3")
@SQLDelete(sql = "UPDATE acg_order SET deleted = true WHERE id = ? AND version = ?")
public class AcgOrder extends BusinessOrderBase implements PayableBusinessOrder {

    @Valid
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Size(min = 1)
    List<AcgGoods> goods;

    @NotNull
    @Mutable
    @Column(columnDefinition = "VARCHAR(10)")
    private Currency currency;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "shipping_info_id")
    private AcgShippingInfo shippingInfo;

    @NotNull
    @Convert(converter = Contact.ContactConverter.class)
    @Column(columnDefinition = "TEXT")
    private Contact contact;

    @NotNull
    @Mutable
    private Price price;

    @Mutable
    private String delegateOrderId;

    @Mutable
    private String delegateOrderPayId;

    private String agentCode;

    @OneToOne(mappedBy = "acgOrder", targetEntity = TransportPlan.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private TransportPlan transportPlan;

    @Column(name = "acg_primary_number")
    private String acgPrimaryNum;

    @OneToMany
    @JoinColumn(name = "orderId")
    @OrderBy("id DESC")
    List<OrderLogistic> logisticsStatus;

    // 航空货运订单子类型，存放枚举name，用于程序
    @Override
    public List<String> getOrderSubTypes() {
        ArrayList<String> subTypes = Lists.newArrayList(ServiceType.AirCargo.name());
        if (shippingInfo.getDeparture().isDelivery()) {
            subTypes.add(ServiceType.PickUp.name());
        }
        if (shippingInfo.getArrival().isDelivery()) {
            subTypes.add(ServiceType.DropOff.name());
        }
        return subTypes;
    }

    // 航空货运订单子类型，存放枚举name，用于前端显示
    @Override
    public List<String> getOrderSubTypesForDisplay() {
        ArrayList<String> subTypes = Lists.newArrayList(ServiceType.AirCargo.getDescription());
        if (shippingInfo.getDeparture().isDelivery()) {
            subTypes.add(ServiceType.PickUp.getDescription());
        }
        if (shippingInfo.getArrival().isDelivery()) {
            subTypes.add(ServiceType.DropOff.getDescription());
        }
        return subTypes;
    }

    @PreUpdate
    @PrePersist
    public void validate() {
        if (CollectionUtils.isEmpty(goods)) {
            throw new ConstraintViolationException("goods count can't be empty", null);
        }
    }

    @Override
    @JsonIgnore
    public BigDecimal getTotalPrice() {
        return price.getTotal();
    }

    public enum ServiceType {
        PickUp("上门取货"),
        DropOff("机场派送"),
        AirCargo("航空货运");

        private String description;

        ServiceType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Price {
        @NotNull
        @DecimalMin("0.00")
        private BigDecimal airlineFee;

        @DecimalMin("0.00")
        private BigDecimal pickUpFee;

        @DecimalMin("0.00")
        private BigDecimal dropOffFee;

        @DecimalMin("0.00")
        @Column(name = "total_fee")
        private BigDecimal total;
    }
}


