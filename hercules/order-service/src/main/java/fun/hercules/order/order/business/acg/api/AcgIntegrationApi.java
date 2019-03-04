package fun.hercules.order.order.business.acg.api;

import fun.hercules.order.order.business.acg.domain.AcgAirport;
import fun.hercules.order.order.business.acg.domain.AcgCompleteRequest;
import fun.hercules.order.order.business.acg.domain.PriceRequest;
import fun.hercules.order.order.business.acg.domain.PriceResponse;
import fun.hercules.order.order.business.acg.domain.TransportPlan;
import fun.hercules.order.order.business.acg.domain.UpdateLogisticStatusRequest;
import fun.hercules.order.order.business.acg.dto.AcgResponse;
import fun.hercules.order.order.business.acg.domain.AcgOrder;
import fun.hercules.order.order.business.acg.dto.booked.AcgBookedRequest;
import fun.hercules.order.order.business.acg.dto.plan.AcgPlanRequest;
import fun.hercules.order.order.business.acg.service.AcgAirportImportService;
import fun.hercules.order.order.business.acg.service.AcgAirportService;
import fun.hercules.order.order.business.acg.service.AcgIntegrationService;
import fun.hercules.order.order.business.acg.service.AcgOrderService;
import fun.hercules.order.order.platform.order.model.OrderStatus;
import fun.hercules.order.order.platform.order.model.PaymentRequest;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/acg-api", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "/acg-api", description = "Access to airports api")
public class AcgIntegrationApi {
    private final AcgAirportService airportService;

    private final AcgOrderService acgOrderService;

    private final AcgAirportImportService airportImporterService;

    private final AcgIntegrationService integrationService;

    public AcgIntegrationApi(AcgAirportService airportService,
                             AcgOrderService acgOrderService, AcgAirportImportService airportImporterService,
                             AcgIntegrationService integrationService) {
        this.airportService = airportService;
        this.acgOrderService = acgOrderService;
        this.airportImporterService = airportImporterService;
        this.integrationService = integrationService;
    }

    @GetMapping(path = "/airports")
    public List<AcgAirport> getAirports() {
        return airportService.list();
    }

    @PostMapping(path = "/airports")
    @ResponseStatus(HttpStatus.CREATED)
    public void importAirports(@RequestParam("airportInfoFile") MultipartFile airportInfoFile, @RequestParam("airportNameFile") MultipartFile airportNameFile) {
        airportImporterService.execute(airportInfoFile, airportNameFile);
    }

    @PostMapping(path = "/order-price")
    public PriceResponse getOrderPrice(@RequestBody PriceRequest request) {
        return integrationService.getOrderPrice(request);
    }

    @GetMapping(path = "/orders/{id}/payment-status")
    public PaymentRequest getOrderPaymentStatus(@PathVariable("id") String orderId) {
        return acgOrderService.getPaymentRequest(orderId);
    }

    @PostMapping(path = "/orders/{id}/completed")
    AcgResponse complete(@PathVariable String id,
                         @RequestBody AcgCompleteRequest request) {
        return acgOrderService.onComplete(id, request);
    }

    @PostMapping(path = "/orders/{orderId}/booked")
    AcgResponse updateBookedStatus(@PathVariable("orderId") String orderId,
                                   @RequestBody AcgBookedRequest request) {
        AcgOrder acgOrder = acgOrderService.get(orderId);
        acgOrder.setStatus(OrderStatus.of(OrderStatus.Type.WaitForPay));
        acgOrderService.update(acgOrder);
        return AcgResponse.success();
    }

    @PostMapping(path = "orders/{orderId}/logistics")
    public AcgResponse updateLogisticsStatus(@PathVariable("orderId") String orderId,
                                      @RequestBody UpdateLogisticStatusRequest request) {
        return acgOrderService.updateLogisticsStatus(orderId, request);
    }

    @PostMapping(path = "/orders/{orderId}/plan")
    AcgResponse updatePlan(@PathVariable("orderId") String orderId,
                           @RequestBody AcgPlanRequest request) {
        AcgOrder acgOrder = acgOrderService.get(orderId);
        TransportPlan transportPlan = new TransportPlan(request);
        acgOrder.setTransportPlan(transportPlan);
        acgOrder.setDelegateOrderId(request.getDelegateOrderId());
        transportPlan.setAcgOrder(acgOrder);
        acgOrderService.update(acgOrder);
        return AcgResponse.success();
    }


}
