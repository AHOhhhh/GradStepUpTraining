package fun.hercules.order.order.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderNotification {

    private String enterpriseId;

    private NotificationType notificationType;

    private String orderId;

    private String name;

    private String orderType;

    private String orderInfo;

    private String status;

    private String description;

    private String serviceTypes;
}
