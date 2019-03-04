package fun.hercules.mockserver.acg.order.clients;

import fun.hercules.mockserver.acg.order.model.AcgResponse;
import fun.hercules.mockserver.acg.order.model.booked.AcgBookedRequest;
import fun.hercules.mockserver.acg.order.model.complete.AcgCompleteRequest;
import fun.hercules.mockserver.acg.order.model.logisticstatus.UpdateLogisticStatusRequest;
import fun.hercules.mockserver.acg.order.model.plan.AcgPlanRequest;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "openapi-service", url = "${hlp.endpoints.order}")
public interface AcgBusinessClient {
    @RequestMapping(method = RequestMethod.POST, path = "/acg-api/orders/{orderId}/booked")
    AcgResponse updateBookedStatus(@PathVariable("orderId") String orderId,
                                         @RequestBody AcgBookedRequest request);

    @RequestMapping(method = RequestMethod.POST, path = "/acg-api/orders/{orderId}/plan")
    AcgResponse updatePlan(@PathVariable("orderId") String orderId,
                           @RequestBody AcgPlanRequest request);

    @RequestMapping(method = RequestMethod.POST, path = "/acg-api/orders/{orderId}/completed")
    AcgResponse complete(@PathVariable("orderId") String orderId,
                                            @RequestBody AcgCompleteRequest request);

    @RequestMapping(method = RequestMethod.POST, path = "/acg-api/orders/{orderId}/logistics")
    AcgResponse updateLogisticsStatus(@PathVariable("orderId") String orderId,
                                                       @RequestBody UpdateLogisticStatusRequest request);
}
