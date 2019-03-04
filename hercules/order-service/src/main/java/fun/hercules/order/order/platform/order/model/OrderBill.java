package fun.hercules.order.order.platform.order.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableBiMap;
import fun.hercules.order.order.common.serialize.OrderTypeToStringSerializer;
import fun.hercules.order.order.common.serialize.PayChannelSerializer;
import fun.hercules.order.order.common.serialize.PayMethodSerializer;
import fun.hercules.order.order.common.serialize.VendorToStringSerializer;
import fun.hercules.order.order.platform.exports.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = {"updatedAt", "createdBy", "updatedBy"})
@Where(clause = EntityBase.SKIP_DELETED_CLAUSE)
@SQLDelete(sql = "UPDATE order_bill SET deleted = true WHERE id = ? AND version = ?")
public class OrderBill extends EntityBase {

    public static final ImmutableBiMap<String, String> orderTypeMap = ImmutableBiMap.<String, String>builder().put("wms", "仓储管理").put("acg", "航空货运").build();

    public static final ImmutableBiMap<String, String> vendorMap = ImmutableBiMap.<String, String>builder().put("wms", "WMS").put("yzra", "大力神货运").build();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String orderId;
    @JsonSerialize(using = OrderTypeToStringSerializer.class)
    private String orderType;
    @JsonSerialize(using = VendorToStringSerializer.class)
    private String vendor;

    @JsonSerialize(using = PayMethodSerializer.class)
    @Enumerated(value = EnumType.STRING)
    private PayMethod payMethod;

    @JsonSerialize(using = PayChannelSerializer.class)
    @Enumerated(value = EnumType.STRING)
    private PayChannel payChannel;

    @Builder.Default
    private BigDecimal productCharge = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal serviceCharge = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal commissionCharge = BigDecimal.ZERO;
    @Builder.Default
    private Currency currency = Currency.getInstance("CNY");

    private boolean productChargeSettled;
    private boolean serviceChargeSettled;
    private boolean commissionChargeSettled;

    @Override
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss'Z'", timezone = "UTC")
    public Instant getCreatedAt() {
        return super.getCreatedAt();
    }

    public void initDefaultStatus() {
        productChargeSettled = false;
        serviceChargeSettled = payChannel == PayChannel.WMS;
        commissionChargeSettled = false;
    }

    public void paySuccess() {
        this.productChargeSettled = true;
    }

}
