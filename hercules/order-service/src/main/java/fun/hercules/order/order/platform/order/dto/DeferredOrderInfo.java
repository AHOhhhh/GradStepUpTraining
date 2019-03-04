package fun.hercules.order.order.platform.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DeferredOrderInfo{

    private String paymentId;

    private BigDecimal orderAmount;

    private BigDecimal payAmount;

    private String currencyCode;

    private String payCustId;

    private String payCustName;

}
