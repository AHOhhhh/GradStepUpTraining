package fun.hercules.order.order.platform.order.web;

import fun.hercules.order.order.business.acg.client.AcgBusinessClient;
import fun.hercules.order.order.business.acg.domain.AcgOrder;
import fun.hercules.order.order.business.acg.dto.AcgResponse;
import fun.hercules.order.order.business.acg.dto.order.AcgCreateOrderResponse;
import fun.hercules.order.order.business.acg.service.AcgIntegrationService;
import fun.hercules.order.order.platform.exports.BusinessOrder;
import fun.hercules.order.order.platform.exports.api.OrderServiceAPI;
import fun.hercules.order.order.platform.order.dto.Cancellation;
import fun.hercules.order.order.platform.order.dto.OrderPageResponse;
import fun.hercules.order.order.platform.order.model.OrderType;
import fun.hercules.order.order.platform.order.model.Payment;
import fun.hercules.order.order.platform.order.service.OrderRenderService;
import fun.hercules.order.order.platform.order.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/{orderType:.+}/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "{type}/orders", description = "Access to order")
public class OrderController {

    private final OrderServiceAPI orderService;

    private final PaymentService paymentService;

    private final OrderRenderService orderRenderService;

    private final AcgIntegrationService acgIntegrationService;

    public OrderController(OrderServiceAPI orderService,
                           PaymentService paymentService, OrderRenderService orderRenderService, AcgBusinessClient acgBusinessClient, AcgIntegrationService acgIntegrationService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.orderRenderService = orderRenderService;
        this.acgIntegrationService = acgIntegrationService;
    }

    @RequestMapping(method = RequestMethod.GET, params = "ids")
    public List<BusinessOrder> getOrdersById(@PathVariable("orderType") String type,
                                             @RequestParam("ids") Set<String> ids) {
        if (OrderType.Type.ACG.getValue().equals(type)) {
            //查询航空货运运单，该接口不需要任何权限
            return orderService.listAcgOrderByIds(ids);
        } else {
            return orderService.listOrderByTypeAndIds(type, ids);
        }
    }


    @RequestMapping(method = RequestMethod.GET)
    public OrderPageResponse<BusinessOrder> getOrders(@PathVariable("orderType") String type,
                                                      @RequestParam(required = false) String enterpriseId,
                                                      @RequestParam(required = false, defaultValue = "All") String status,
                                                      @RequestParam(required = false, defaultValue = "0") int page,
                                                      @RequestParam(required = false, defaultValue = "10") int size) {
        OrderPageResponse<BusinessOrder> orders = orderService.list(enterpriseId, type, status, page, size);
        orders.setContent(orders.getContent().stream()
                .map(orderRenderService::renderOrder)
                .collect(Collectors.toList()));
        return orders;

    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "create new order")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "order created"),
            @ApiResponse(code = 400, message = "invalid order information"),
            @ApiResponse(code = 409, message = "order conflicts")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public BusinessOrder createOrder(@PathVariable("orderType") String type,
                                     @RequestBody String rawOrder) {
        BusinessOrder businessOrder = orderService.create(type, rawOrder);
        if (type.equals("acg")) {
            return acgIntegrationService.createOrder((AcgOrder) businessOrder).getStatus().equals(AcgResponse.Status.SUCCESS) ? businessOrder : null;
        }
        return businessOrder;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    @ApiOperation(value = "get order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "get order successfully"),
            @ApiResponse(code = 404, message = "No order matches given id")
    })
    public BusinessOrder getOrder(@PathVariable("orderType") String type,
                                  @PathVariable("id") String orderId) {
        return orderRenderService.renderOrder(orderService.get(type, orderId));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/status-transitions", produces = "image/svg+xml")
    @ApiOperation(value = "get order status transitions")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "get order status transitions successfully"),
            @ApiResponse(code = 404, message = "No order type matches given id")
    })
    public ResponseEntity getOrderTransitions(@PathVariable("orderType") String type) {
        // 查看各个业态订单状态变化的流程
        return ResponseEntity.ok(orderService.renderOrderTransitions(type));
    }


    @RequestMapping(method = RequestMethod.POST,
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "updatePaymentStatus order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "get order successfully"),
            @ApiResponse(code = 400, message = "invalid order information"),
            @ApiResponse(code = 404, message = "No order matches given id")
    })
    public BusinessOrder updateOrder(@PathVariable("orderType") String type,
                                     @PathVariable("id") String orderId,
                                     @RequestBody String rawOrder) {
        return orderService.update(type, orderId, rawOrder);
    }


    @RequestMapping(method = RequestMethod.POST,
            path = "/{id}/deletion",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "delete order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "delete order successfully"),
            @ApiResponse(code = 404, message = "No order matches given id")
    })
    public void deleteOrder(@PathVariable("orderType") String type,
                            @PathVariable("id") String orderId) {
        orderService.delete(type, orderId);
    }

    @RequestMapping(method = RequestMethod.POST,
            path = "/{id}/cancellation",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "cancel order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "cancel order successfully"),
            @ApiResponse(code = 404, message = "No order matches given id")
    })
    public BusinessOrder cancelOrder(@PathVariable("orderType") String type,
                                     @PathVariable("id") String orderId,
                                     @RequestBody Cancellation cancellation) {
        return orderService.cancel(type, orderId, cancellation.getCancelReason());
    }

    @GetMapping(path = "/{id}/paymentId")
    @ApiOperation(value = "get order payment ids")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "get order payment ids successfully"),
            @ApiResponse(code = 404, message = "No order matches given id")
    })
    @ResponseStatus(HttpStatus.OK)
    public Payment getPaymentByOrderId(@PathVariable("orderType") String type,
                                       @PathVariable("id") String orderId) {
        List<Payment> paymentList = paymentService.findByOrderId(orderId);
        return CollectionUtils.isNotEmpty(paymentList) ? paymentList.get(0) : new Payment();
    }

}
