package fun.hercules.order.order.clients.service;

import fun.hercules.order.order.clients.UserServiceClient;
import fun.hercules.order.order.common.dto.OrderNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderNotificationService {
    private UserServiceClient userServiceClient;

    public OrderNotificationService(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Async
    public void create(OrderNotification orderNotification) {
        userServiceClient.createOrderNotification(orderNotification);
    }
}
