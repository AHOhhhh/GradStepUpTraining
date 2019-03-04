package fun.hercules.mockserver.acg.order.web;

import fun.hercules.mockserver.acg.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(path = "/acg-mock")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String createOrder(Model model) {
        log.info("{}", orderService.listOrders());
        model.addAttribute("orders", orderService.listOrders());
        return "orders";
    }
}