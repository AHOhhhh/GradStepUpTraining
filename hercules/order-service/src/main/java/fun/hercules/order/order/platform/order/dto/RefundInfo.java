package fun.hercules.order.order.platform.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundInfo {

    private String paymentId;

    private BigDecimal payAmount;

    private String payCustId;

    private String payCustName;

    private String currencyCode;

    private String comment;
}
