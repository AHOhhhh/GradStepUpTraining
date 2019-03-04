package fun.hercules.order.order.business.acg.client;

import fun.hercules.order.order.business.acg.dto.AcgResponse;
import fun.hercules.order.order.business.acg.dto.order.AcgCreateOrderRequest;
import fun.hercules.order.order.business.acg.dto.order.AcgCreateOrderResponse;
import fun.hercules.order.order.business.acg.dto.price.AcgGetPriceRequest;
import fun.hercules.order.order.business.acg.dto.price.AcgGetPriceResponse;
import fun.hercules.order.order.common.signature.annotations.ApiSignature;
import fun.hercules.order.order.platform.order.dto.payment.AcgUpdateOrderToPaidRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// Standard client to connect with ACG service interface - future function
@ApiSignature("acg")
@FeignClient(value = "acg-business-service", url = "${hlp.business-services.acg.integration.url}")
public interface AcgBusinessClient {
    @RequestMapping(method = RequestMethod.POST, path = "/api/v1/acg/prices")
    AcgGetPriceResponse getPrice(@RequestBody AcgGetPriceRequest request);


    @RequestMapping(method = RequestMethod.POST, path = "/api/v1/acg/orders")
    AcgCreateOrderResponse createOrder(@RequestBody AcgCreateOrderRequest request);

    @PostMapping(path = "/api/v1/acg/orders/{orderId}/payment")
    AcgResponse updateOrderToPaid(@PathVariable("orderId") String orderId,
                                  @RequestBody AcgUpdateOrderToPaidRequest request);
}
