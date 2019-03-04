package fun.hercules.mockserver.acg.order.api;

import fun.hercules.mockserver.acg.order.model.AcgResponse;
import fun.hercules.mockserver.acg.order.model.cancel.AcgCancelRequest;
import fun.hercules.mockserver.acg.order.model.create.AcgCreateOrderRequest;
import fun.hercules.mockserver.acg.order.model.create.AcgCreateOrderResponse;
import fun.hercules.mockserver.acg.order.model.payment.AcgUpdateOrderToPaidRequest;
import fun.hercules.mockserver.acg.order.model.price.AcgGetPriceRequest;
import fun.hercules.mockserver.acg.order.model.price.AcgGetPriceResponse;
import fun.hercules.mockserver.acg.order.service.OrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("ACG Producer API")
@RestController
@RequestMapping(path = "/api/v1/acg",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ConsumerAPI {
    private final OrderService orderService;

    @Autowired
    public ConsumerAPI(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(path = "/prices")
    public AcgGetPriceResponse getPrice(@RequestBody AcgGetPriceRequest request) {
        return orderService.getPrice(request);
    }

    @PostMapping(path = "/orders")
    public AcgCreateOrderResponse createOrder(@RequestBody AcgCreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @PostMapping(path = "/orders/{orderId}/payment")
    AcgResponse updateOrderToPaid(@PathVariable("orderId") String orderId,
                                  @RequestBody AcgUpdateOrderToPaidRequest request) {
        return orderService.updateOrderToPaid(orderId, request);
    }
    @RequestMapping(method = RequestMethod.POST, path = "/orders/{id}/cancel")
    AcgResponse cancelOrder(@PathVariable("id") String orderId,
                            @RequestBody AcgCancelRequest request) {
        return orderService.cancelOrder(orderId, request);
    }
}