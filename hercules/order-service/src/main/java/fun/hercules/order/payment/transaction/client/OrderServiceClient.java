package fun.hercules.order.payment.transaction.client;

import fun.hercules.order.payment.support.OrderClientFeignConfiguration;
import fun.hercules.order.payment.transaction.response.PaymentNotification;
import fun.hercules.order.payment.transaction.response.TransactionNotification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "order-service-client", url = "${hlp.endpoints.order}", configuration = OrderClientFeignConfiguration.class)
public interface OrderServiceClient {

    @PostMapping(path = "/order-payment/{paymentId}")
    void updateOrderStatus(@RequestBody PaymentNotification notification, @PathVariable("paymentId") String paymentId);

    @PostMapping(path = "/order-refunds/{paymentId}")
    void createRefundTransaction(@RequestBody TransactionNotification notification, @PathVariable("paymentId") String paymentId);
}
