package fun.hercules.mockserver.acg.order.web;

import fun.hercules.mockserver.acg.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/acg-mock/order-triggers")
public class TriggerAPI {
    private final OrderService orderService;

    @Autowired
    public TriggerAPI(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{orderId}")
    public void triggerOrder(@PathVariable String orderId) {
        orderService.trigger(orderId);
    }
}