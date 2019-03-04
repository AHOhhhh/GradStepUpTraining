package fun.hercules.order.order.configurations;


import fun.hercules.order.order.platform.order.model.OrderType.Type;
import fun.hercules.order.order.platform.order.model.PayMethod;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "pay-method")
@Data
public class OrderPayMethod {
    private List<PayMethod> wms;
    private List<PayMethod> acg;

    public List<PayMethod> getPayMethods(String orderType) {
        Type type = Type.valueOf(orderType);
        switch (type) {
            case WMS:
                return wms;
            case ACG:
                return acg;
            default:
                return new ArrayList<>();
        }
    }
}
