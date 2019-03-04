package fun.hercules.order.order.platform.order.web;

import com.google.common.collect.Lists;
import fun.hercules.order.order.common.errors.ErrorCode;
import fun.hercules.order.order.common.exceptions.UnauthorizedException;
import fun.hercules.order.order.platform.order.converter.OrderBillConverter;
import fun.hercules.order.order.platform.order.dto.OrderBillDTO;
import fun.hercules.order.order.platform.order.dto.UploadExcelResponse;
import fun.hercules.order.order.platform.order.model.OrderBill;
import fun.hercules.order.order.platform.order.service.OrderBillExcelService;
import fun.hercules.order.order.platform.order.service.OrderBillService;
import fun.hercules.order.order.platform.order.service.OrderService;
import fun.hercules.order.order.platform.order.service.OrderTypeAccessHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/order-bill", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "/order-bill", description = "Access to order bill")
public class OrderBillController {

    private final OrderBillService orderBillService;

    private final OrderTypeAccessHandler orderTypeAccessHandler;

    private final OrderService orderService;

    private final OrderBillExcelService excelService;

    private final OrderBillConverter orderBillConverter;

    @Autowired
    public OrderBillController(OrderBillService orderBillService,
                               OrderTypeAccessHandler orderTypeAccessHandler,
                               OrderService orderService,
                               OrderBillExcelService excelService,
                               OrderBillConverter orderBillConverter) {
        this.orderBillService = orderBillService;
        this.orderTypeAccessHandler = orderTypeAccessHandler;
        this.orderService = orderService;
        this.excelService = excelService;
        this.orderBillConverter = orderBillConverter;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('PlatformAdmin')")
    public Page<OrderBillDTO> findOrderBills(@SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {
        return orderBillService.findPageableOrderBills(dateFrom, dateTo, orderTypeAccessHandler.validateAndGetPermittedOrderTypes(), pageable)
                .map(orderBillConverter::convert);
    }

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('PlatformAdmin')")
    public OrderBillDTO findOrderBill(@PathVariable("orderId") String orderId) {
        if (orderTypeAccessHandler.validateAndGetPermittedOrderTypes().contains(orderService.getByOrderId(orderId).getOrderType())) {
            return orderBillConverter.convert(orderBillService.findOrderBillByOrderId(orderId));
        }
        throw new UnauthorizedException(ErrorCode.ORDER_ACCESS_DENIED);
    }

    @GetMapping("/excel")
    @PreAuthorize("hasAnyAuthority('PlatformAdmin')")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "download excel")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "download successful"),
            @ApiResponse(code = 500, message = "create excel error")
    })
    public void downloadExcel(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
                              @RequestParam(required = false) String orderId,
                              HttpServletResponse response) {
        List<String> headerKeys = Arrays.asList("orderId", "createdAt", "orderType", "vendor", "payMethod", "payChannel", "productCharge", "productChargeStatus",
                "serviceCharge", "serviceChargeStatus", "commissionCharge", "commissionChargeStatus", "currency");
        List<OrderBill> orderBills = orderBillService.findAll(dateFrom, dateTo, orderId, orderTypeAccessHandler.validateAndGetPermittedOrderTypes());
        List<OrderBillDTO> orderBillDtos = Lists.newArrayList(orderBillConverter.convertAll(orderBills));

        excelService.writeExcel(response, headerKeys, orderBillDtos);
    }

    @PostMapping("/excel")
    @PreAuthorize("hasAnyAuthority('PlatformAdmin')")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "parse excel")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "parse excel successful"),
            @ApiResponse(code = 413, message = "file too large error"),
            @ApiResponse(code = 500, message = "parse excel error")
    })
    public UploadExcelResponse uploadExcel(@RequestParam("fileUpload") MultipartFile multipartFile) {
        List<Map<String, Object>> bodyList = excelService.getExcelBodyList(multipartFile);
        UploadExcelResponse result = UploadExcelResponse.builder().build();
        if (CollectionUtils.isEmpty(bodyList)) {
            return result;
        }
        List<String> uniqueOrderIds = bodyList.stream()
                .filter(body -> body.get("orderId") != null)
                .map(item -> item.get("orderId").toString())
                .distinct().collect(Collectors.toList());

        List<OrderBill> orderBills = orderBillService.getByOrderIds(uniqueOrderIds, orderTypeAccessHandler.validateAndGetPermittedOrderTypes());

        excelService.validate(bodyList, orderBills, result);
        result.setCount(result.isSucceed() ? excelService.updateOrderBills(bodyList, orderBills).size() : 0);

        return result;
    }

}
