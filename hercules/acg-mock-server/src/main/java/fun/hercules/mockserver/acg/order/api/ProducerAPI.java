package fun.hercules.mockserver.acg.order.api;

import fun.hercules.mockserver.acg.order.model.AcgResponse;
import fun.hercules.mockserver.acg.order.model.booked.AcgBookedRequest;
import fun.hercules.mockserver.acg.order.model.complete.AcgCompleteRequest;
import fun.hercules.mockserver.acg.order.model.logisticstatus.UpdateLogisticStatusRequest;
import fun.hercules.mockserver.acg.order.model.plan.AcgPlanRequest;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("ACG Producer API")
@RestController
@RequestMapping(path = "/acg-api/orders/{orderId}",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProducerAPI {
    @RequestMapping(method = RequestMethod.POST, path = "/booked")
    AcgResponse updateBookedStatus(@PathVariable("orderId") String orderId,
                                   @RequestBody AcgBookedRequest request) {

        return AcgResponse.success();
//        throw new UnsupportedOperationException("for api docs only");
    }

    @RequestMapping(method = RequestMethod.POST, path = "/plan")
    AcgResponse updatePlan(@PathVariable("orderId") String orderId,
                           @RequestBody AcgPlanRequest request) {
        throw new UnsupportedOperationException("for api docs only");
    }

    @RequestMapping(method = RequestMethod.POST, path = "/completed")
    AcgResponse complete(@PathVariable("orderId") String orderId,
                         @RequestBody AcgCompleteRequest request) {
        throw new UnsupportedOperationException("for api docs only");
    }

    @RequestMapping(method = RequestMethod.POST, path = "/logistics")
    AcgResponse updateLogisticsStatus(@PathVariable("orderId") String orderId,
                                      @RequestBody UpdateLogisticStatusRequest request) {
        throw new UnsupportedOperationException("for api docs only");
    }
}