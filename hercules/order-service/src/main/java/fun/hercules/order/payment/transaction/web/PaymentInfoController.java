package fun.hercules.order.payment.transaction.web;

import fun.hercules.order.payment.transaction.domain.OfflinePayTrans;
import fun.hercules.order.payment.transaction.request.OfflineOrderInfo;
import fun.hercules.order.payment.transaction.service.OfflinePayTransService;
import fun.hercules.order.payment.transaction.validators.OrderInfoValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping(value = "/payment-info")
@Api(value = "get payment info", description = "get payment info")
public class PaymentInfoController {

    private final OfflinePayTransService offlinePayTransService;

    private final OrderInfoValidator orderInfoValidator;

    @Autowired
    public PaymentInfoController(OfflinePayTransService offlinePayTransService,
                                 OrderInfoValidator orderInfoValidator) {
        this.offlinePayTransService = offlinePayTransService;
        this.orderInfoValidator = orderInfoValidator;
    }

    @PostMapping(value = "/offline")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "create offline pay trans")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "create offline pay trans"),
            @ApiResponse(code = 500, message = "Failed to create new offline pay trans"),
            @ApiResponse(code = 403, message = "unAuthorised")
    })
    @PreAuthorize("hasAnyAuthority('PlatformService')")
    public void createOfflinePaymentTransaction(@RequestBody OfflineOrderInfo offlineOrderInfo) {
        orderInfoValidator.validate(offlineOrderInfo);
        offlinePayTransService.createOfflineTrans(offlineOrderInfo);
    }

    @GetMapping(value = "/offline")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "find offline pay trans")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "find offline pay trans"),
            @ApiResponse(code = 500, message = "Failed to find offline pay trans"),
            @ApiResponse(code = 403, message = "unAuthorised")
    })
    @PreAuthorize("hasAnyAuthority('PlatformService')")
    public OfflinePayTrans findOfflinePaymentTransaction(@RequestParam(value = "paymentId") String paymentId) {
        return offlinePayTransService.findOneByPaymentId(paymentId);
    }
}
