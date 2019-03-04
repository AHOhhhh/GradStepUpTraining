package fun.hercules.order.order.platform.order.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Builder()
@Data
public class OrderBillDTO {
    private String orderId;
    private String orderType;
    private String vendor;
    private String payMethod;
    private String payChannel;
    private BigDecimal productCharge;
    private BigDecimal serviceCharge;
    private BigDecimal commissionCharge;
    private String productChargeStatus;
    private String serviceChargeStatus;
    private String commissionChargeStatus;
    private Instant createdAt;
    private String currency;
}
