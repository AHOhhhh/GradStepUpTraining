package fun.hercules.order.order.platform.order.client;

import fun.hercules.order.order.common.signature.FeignPaymentConfig;
import fun.hercules.order.order.platform.order.dto.DeferredNotifyRequest;
import fun.hercules.order.order.platform.order.dto.DeferredOrderInfo;
import fun.hercules.order.order.platform.order.dto.DeferredTransaction;
import fun.hercules.order.order.platform.order.dto.OfflineNotifyRequest;
import fun.hercules.order.order.platform.order.dto.OfflinePaymentInfo;
import fun.hercules.order.order.platform.order.dto.OfflineTransaction;
import fun.hercules.order.order.platform.order.dto.RefundInfo;
import fun.hercules.order.order.platform.order.dto.TransactionNotification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "payment-client", url = "${hlp.endpoints.order}", configuration = FeignPaymentConfig.class)
public interface PaymentClient {
    @GetMapping(path = "/payment-transactions")
    List<TransactionNotification> getPaymentTransactions(@RequestParam("paymentId") String paymentId);

    @PostMapping(path = "/payment-info/offline")
    void submitOfflinePayment(@RequestBody OfflinePaymentInfo offlinePaymentInfo);

    @GetMapping(path = "/payment-transactions/offline")
    List<OfflineTransaction> getOfflinePaymentTransactions(@RequestParam("paymentId") String paymentId);

    @PostMapping(path = "/payment-info/refund")
    void createRefundTransaction(@RequestBody RefundInfo refundInfo);

    @PostMapping(path = "/payment-transactions/payment-status/deferment")
    List<DeferredTransaction> updateDeferredPayTransStatus(@RequestBody DeferredNotifyRequest request);

    @PostMapping(path = "/payment-info/deferment")
    void createDeferredPayTrans(@RequestBody DeferredOrderInfo request);

    @PostMapping(path = "/payment-transactions/payment-status/offline")
    void updateOfflineTransactionStatus(@RequestBody OfflineNotifyRequest request);

}
