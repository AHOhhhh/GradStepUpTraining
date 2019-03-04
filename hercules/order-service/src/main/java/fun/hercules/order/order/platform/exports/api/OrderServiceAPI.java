package fun.hercules.order.order.platform.exports.api;

import fun.hercules.order.order.platform.order.service.OrderService;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderServiceAPI {

    @Delegate
    private OrderService orderService;

    public OrderServiceAPI(OrderService orderService) {
        this.orderService = orderService;
    }

}
