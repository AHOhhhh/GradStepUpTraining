package fun.hercules.order.payment.transaction.web;


import fun.hercules.order.payment.transaction.domain.OfflinePayTrans;
import fun.hercules.order.payment.transaction.domain.PaymentMethod;
import fun.hercules.order.payment.transaction.domain.RefundTrans;
import fun.hercules.order.payment.transaction.domain.TransactionType;
import fun.hercules.order.payment.transaction.request.OfflineNotifyRequest;
import fun.hercules.order.payment.transaction.response.OfflineTransactionInfo;
import fun.hercules.order.payment.transaction.response.RefundTransactionInfo;
import fun.hercules.order.payment.transaction.response.TransactionInfo;
import fun.hercules.order.payment.transaction.service.OfflinePayTransService;
import fun.hercules.order.payment.transaction.service.RefundTransService;
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

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping(value = "/payment-transactions")
@Api(value = "transaction", description = "access to transactions")
public class TransactionController {

    private final OfflinePayTransService offlinePayTransService;

    private final RefundTransService refundTransService;

    @Autowired
    public TransactionController(OfflinePayTransService offlinePayTransService,
                                 RefundTransService refundTransService) {
        this.offlinePayTransService = offlinePayTransService;
        this.refundTransService = refundTransService;
    }

    @PostMapping("/payment-status/offline")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(value = "update offline transaction status")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "update offline transaction status"),
            @ApiResponse(code = 404, message = "not found")
    })
    @PreAuthorize("hasAnyAuthority('PlatformAdmin', 'PlatformService')")
    public void updateOfflineTransactionStatus(@RequestBody OfflineNotifyRequest request) {
        offlinePayTransService.updateTransStatus(request);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "get transactions by paymentId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = List.class, message = "get transactions successful")})
    @PreAuthorize("hasAnyAuthority('PlatformService')")
    public List<TransactionInfo> getTransactions(@RequestParam String paymentId) {
        List<OfflinePayTrans> offlinePayTransList = offlinePayTransService.findByPaymentId(paymentId);
        List<RefundTrans> refundTransList = refundTransService.findByPaymentId(paymentId);

        List<OfflineTransactionInfo> offlineTransInfos = getOfflineTrans(paymentId, offlinePayTransList);
        List<RefundTransactionInfo> refundTransInfos = getRefundTrans(paymentId, refundTransList);

        List<TransactionInfo> transactionInfos = new ArrayList<>();
        transactionInfos.addAll(offlineTransInfos);
        transactionInfos.addAll(refundTransInfos);

        return transactionInfos.stream().sorted((trans1, trans2) -> trans2.getCreatedAt().compareTo(trans1.getCreatedAt())).collect(toList());
    }

    @GetMapping("/offline")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "get offline transactions by paymentId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = List.class, message = "get offline transactions successful")})
    @PreAuthorize("hasAnyAuthority('PlatformService')")
    public List<OfflineTransactionInfo> getOfflinePaymentTransactions(@RequestParam String paymentId) {
        List<OfflinePayTrans> offlinePayTransList = offlinePayTransService.findByPaymentId(paymentId);
        return getOfflineTrans(paymentId, offlinePayTransList);
    }

    private List<OfflineTransactionInfo> getOfflineTrans(String paymentId, List<OfflinePayTrans> offlinePayTransList) {
        return offlinePayTransList.stream().map(offline ->
                OfflineTransactionInfo.builder()
                        .paymentId(paymentId)
                        .payCustId(offline.getPayCustId())
                        .payCustName(offline.getPayCustName())
                        .currency(offline.getCurrencyCode().getCode())
                        .payStatus(offline.getPayStatus())
                        .paidTime(offline.getPaidTime())
                        .comment(offline.getComment())
                        .payMethod(PaymentMethod.OFFLINE)
                        .type(TransactionType.Expense)
                        .createdAt(offline.getCreatedAt())
                        .payAmount(offline.getPayAmount())
                        .transactionId(offline.getId())
                        .bankTransactionComment(offline.getBankTransactionComment())
                        .bankTransactionNumber(offline.getBankTransactionNumber())
                        .bankTransactionTime(offline.getBankTransactionTime())
                        .build())
                .collect(toList());
    }

    private List<RefundTransactionInfo> getRefundTrans(String paymentId, List<RefundTrans> refundTransList) {
        return refundTransList.stream().map(refund ->
                RefundTransactionInfo.builder()
                        .paymentId(paymentId)
                        .payCustId(refund.getPayCustId())
                        .payCustName(refund.getPayCustName())
                        .currency(refund.getCurrencyCode().getCode())
                        .payStatus(refund.getPayStatus())
                        .paidTime(refund.getPaidTime())
                        .comment(refund.getComment())
                        .payMethod(PaymentMethod.OFFLINE)
                        .type(TransactionType.Income)
                        .createdAt(refund.getCreatedAt())
                        .payAmount(refund.getPayAmount())
                        .transactionId(refund.getId())
                        .build())
                .collect(toList());
    }

}
