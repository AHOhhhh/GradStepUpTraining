package fun.hercules.order.order.platform.order.web;

import com.google.common.collect.ImmutableList;
import fun.hercules.order.order.business.acg.service.AcgIntegrationService;
import fun.hercules.order.order.common.exceptions.NotFoundException;
import fun.hercules.order.order.configurations.OrderPaymentAccount;
import fun.hercules.order.order.platform.exports.BusinessOrder;
import fun.hercules.order.order.platform.exports.api.OrderServiceAPI;
import fun.hercules.order.order.platform.order.client.PaymentClient;
import fun.hercules.order.order.platform.order.dto.OfflinePaymentInfo;
import fun.hercules.order.order.platform.order.dto.OfflineTransaction;
import fun.hercules.order.order.platform.order.dto.PaymentNotification;
import fun.hercules.order.order.platform.order.dto.TransactionNotification;
import fun.hercules.order.order.platform.order.model.OperationType;
import fun.hercules.order.order.platform.order.model.PayMethod;
import fun.hercules.order.order.platform.order.model.PayMethodInfo;
import fun.hercules.order.order.platform.order.model.Payment;
import fun.hercules.order.order.platform.order.model.PaymentRequest;
import fun.hercules.order.order.platform.order.model.PaymentStatus;
import fun.hercules.order.order.platform.order.model.PaymentTransaction;
import fun.hercules.order.order.platform.order.model.Refund;
import fun.hercules.order.order.platform.order.service.OperationLogService;
import fun.hercules.order.order.platform.order.service.PaymentService;
import fun.hercules.order.order.platform.order.service.PaymentTransactionService;
import fun.hercules.order.payment.transaction.service.OfflinePayTransService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static fun.hercules.order.order.common.errors.ErrorCode.PAYMENT_NOT_FOUND;
import static fun.hercules.order.order.common.errors.ErrorCode.TRANSACTION_NOT_FOUND;
import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {

    private OfflinePayTransService offlinePayTransService;

    private PaymentService paymentService;

    private PaymentTransactionService paymentTransactionService;

    private PaymentClient paymentClient;

    private OrderPaymentAccount orderPaymentAccount;

    private OrderServiceAPI orderService;

    private OperationLogService operationLogService;

    private AcgIntegrationService acgIntegrationService;

    public PaymentController(OfflinePayTransService offlinePayTransService, PaymentService paymentService,
                             PaymentTransactionService paymentTransactionService,
                             PaymentClient paymentClient,
                             OrderPaymentAccount orderPaymentAccount,
                             OrderServiceAPI orderService,
                             OperationLogService operationLogService, AcgIntegrationService acgIntegrationService) {
        this.offlinePayTransService = offlinePayTransService;
        this.paymentService = paymentService;
        this.paymentTransactionService = paymentTransactionService;
        this.paymentClient = paymentClient;
        this.orderService = orderService;
        this.operationLogService = operationLogService;
        this.orderPaymentAccount = orderPaymentAccount;
        this.acgIntegrationService = acgIntegrationService;
    }

    @PostMapping(path = "/order-payment/offline")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void createOfflinePayment(@RequestBody OfflinePaymentInfo offlinePaymentInfo) {
        paymentService.validateOfflineOrder(offlinePaymentInfo);

        offlinePaymentInfo.setOrderAmount(offlinePaymentInfo.getPayAmount());
        offlinePaymentInfo.setPaymentId(paymentService.createPayment(offlinePaymentInfo).getId());

        orderService.updateStatusToOrderTracking(offlinePaymentInfo.getBusinessType(), offlinePaymentInfo.getOrderId());
        acgIntegrationService.updateOrderToPaid(offlinePaymentInfo);

    }


    @RequestMapping(method = RequestMethod.POST,
            path = "/order-payment/{paymentId}")
    @ApiOperation(value = "update order payment status")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "update order payment status successfully"),
            @ApiResponse(code = 404, message = "No order matches given id")
    })
    @Transactional
    public void updatePaymentStatus(@PathVariable String paymentId, @RequestBody PaymentNotification notification) {
        log.info("Call updatePaymentStatus by url /order-payment/{}", paymentId);
        paymentService.updatePaymentStatus(paymentId, notification);
    }

    @RequestMapping(method = RequestMethod.POST,
            path = "/order-refunds/{paymentId}")
    @ApiOperation(value = "create order refund transaction")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "create order refund transaction successfully"),
            @ApiResponse(code = 404, message = "No order matches given id")
    })
    @Transactional
    public void createRefundTransaction(@PathVariable String paymentId, @RequestBody TransactionNotification notification) {
        paymentService.createRefundTransaction(paymentId, notification);
    }

    @GetMapping("/order-payment/transactions")
    @ApiOperation(value = "find payment transactions")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "find payment transactions successfully"),
            @ApiResponse(code = 404, message = "No payment transactions matches")
    })
    public Page<PaymentTransaction> findPaymentTransactions(
            @SortDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) List<String> enterpriseIds,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) List<PaymentStatus> statuses) {

        LocalDate parsedFromDate = fromDate == null || fromDate.trim().isEmpty() ? null : LocalDate.parse(fromDate);
        LocalDate parsedToDate = fromDate == null || fromDate.trim().isEmpty() ? null : LocalDate.parse(toDate);

        return paymentTransactionService.find(orderId, enterpriseIds, parsedFromDate, parsedToDate, statuses, pageable);
    }


    @GetMapping(path = "/orders/{id}/payment-requests")
    public List<PaymentRequest> getPaymentRequestByOrderId(@PathVariable("id") String orderId) {
        // TODO: 06/12/2017 Need code to create payment-request after order's status changed to WAITING_FOR_PAY
        return this.paymentService.getPaymentRequestsByOrderId(orderId);
    }

    @GetMapping(path = "/orders/{orderType}/payment-methods")
    @ApiOperation(value = "get order payment method")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "get payment method successfully"),
    })
    @ResponseStatus(HttpStatus.OK)
    public List<PayMethodInfo> getPayMethods(@PathVariable("orderType") String orderType) {
        List<PayMethod> userPayMethods = ImmutableList.of(PayMethod.OFFLINE);

        return userPayMethods.stream()
                .map(payMethod -> PayMethodInfo.builder()
                        .payMethod(payMethod)
                        .paymentAccount(orderPaymentAccount.getPaymentAccount(orderType, payMethod))
                        .build()
                ).collect(Collectors.toList());
    }

    @GetMapping(path = "/orders/{orderId}/transactions/latest-transaction")
    @ApiOperation(value = "get latest offline transaction")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "get latest transaction successful"),
            @ApiResponse(code = 404, message = "not found valid data")
    })
    @ResponseStatus(HttpStatus.OK)
    public TransactionNotification getLatestTransaction(@PathVariable("orderId") String orderId, @RequestParam(required = false) String paymentStatus) {
        return null;
    }

    @GetMapping(path = "/orders/{orderId}/transactions")
    @ApiOperation(value = "get transaction")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "get transaction successful"),
            @ApiResponse(code = 404, message = "not found valid data")
    })
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionNotification> getTransaction(@PathVariable("orderId") String orderId) {
        List<Payment> payments = paymentService.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(payments)) {
            return null;
        }
        List<TransactionNotification> paymentTransactions = paymentClient.getPaymentTransactions(payments.get(0).getId());
        if (CollectionUtils.isEmpty(paymentTransactions)) {
            return null;
        }
        return paymentTransactions.stream()
                .filter(t -> PaymentStatus.Success.equals(t.getPayStatus()))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/orders/{orderId}/transactions/offline/latest-transaction")
    @ApiOperation(value = "get latest offline transaction")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "get offline latest transaction successful"),
            @ApiResponse(code = 404, message = "not found valid data")
    })
    @ResponseStatus(HttpStatus.OK)
    public TransactionNotification getOfflineLatestTransaction(@PathVariable("orderId") String orderId) {
        List<Payment> payments = paymentService.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(payments)) {
            throw new NotFoundException(PAYMENT_NOT_FOUND, "can not find payment by orderId " + orderId);
        }
        String paymentId = payments.get(0).getId();
        List<OfflineTransaction> offlinePaymentTransactions = paymentClient.getOfflinePaymentTransactions(paymentId);
        if (CollectionUtils.isEmpty(offlinePaymentTransactions)) {
            throw new NotFoundException(TRANSACTION_NOT_FOUND,
                    "can not find transactions by orderId " + orderId + " paymentId " + paymentId);
        }

        return offlinePaymentTransactions.get(0);
    }

    @PostMapping(path = "orders/{id}/refund")
    @ApiOperation(value = "refund order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "refund order successfully"),
            @ApiResponse(code = 404, message = "No order matches given id")
    })
    public void refundOrder(@PathVariable("id") String orderId,
                            @RequestBody Refund refund) {
        paymentService.refund(orderId, refund);
    }
}
