package fun.hercules.order.order.platform.order.web;

import com.google.common.collect.ImmutableSet;
import fun.hercules.order.order.common.errors.ErrorCode;
import fun.hercules.order.order.common.exceptions.BadRequestException;
import fun.hercules.order.order.platform.exports.BusinessOrder;
import fun.hercules.order.order.platform.order.dto.OrderPageResponse;
import fun.hercules.order.order.platform.order.service.OrderRenderService;
import fun.hercules.order.order.platform.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "/orders", description = "Unified access to order")
public class UnifiedOrderController {

    private final OrderRenderService orderRenderService;

    private final OrderService orderService;

    public UnifiedOrderController(OrderRenderService orderRenderService, OrderService orderService) {
        this.orderRenderService = orderRenderService;
        this.orderService = orderService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public OrderPageResponse getOrders(@RequestParam(required = false) String enterpriseId,
                                       @RequestParam(required = false, defaultValue = "acg") String type,
                                       @RequestParam(required = false, defaultValue = "All") String status,
                                       @RequestParam(required = false, defaultValue = "0") int page,
                                       @RequestParam(required = false, defaultValue = "10") int size) {
        //获取order列表
        if (StringUtils.isEmpty(type)) {
            type = "acg";
        }
        if (!ImmutableSet.of("wms", "acg").contains(type)) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST,
                    String.format("invalid list type %s", type));
        }
        OrderPageResponse<BusinessOrder> orders = orderService.list(enterpriseId, type, status, page, size);

        orders.setContent(orders.getContent().stream()
                .map(orderRenderService::renderOrder)
                .collect(Collectors.toList()));

        return orders;
    }


    //根据orderId获取order
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    @ApiOperation(value = "get order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "get order successfully"),
            @ApiResponse(code = 404, message = "No order matches given id")
    })
    public BusinessOrder getOrder(@PathVariable("id") String orderId) {
        return orderRenderService.renderOrder(orderService.getByOrderId(orderId));
    }
}
