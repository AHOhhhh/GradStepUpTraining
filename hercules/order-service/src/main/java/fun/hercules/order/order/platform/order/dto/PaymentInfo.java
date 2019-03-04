package fun.hercules.order.order.platform.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fun.hercules.order.order.platform.order.model.PayChannel;
import fun.hercules.order.order.platform.order.model.PayMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentInfo {
    private String orderId;
    private List<String> payRequestIds;
    private BigDecimal orderAmount;
    private BigDecimal payAmount;
    private String currencyCode;
    private String businessType;
    private String payCustId;
    private String payCustName;
    private String paymentId;
    private PayChannel payChannel;
    private PayMethod payMethod;
}
