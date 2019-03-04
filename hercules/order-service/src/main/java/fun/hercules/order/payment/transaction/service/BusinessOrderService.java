package fun.hercules.order.payment.transaction.service;

import fun.hercules.order.payment.transaction.client.OrderServiceClient;
import fun.hercules.order.payment.transaction.response.PaymentNotification;
import fun.hercules.order.payment.transaction.response.TransactionNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BusinessOrderService {

    private final OrderServiceClient client;

    @Autowired
    public BusinessOrderService(OrderServiceClient client) {
        this.client = client;
    }

    @Async
    public void updateOrderPaymentStatus(PaymentNotification notification) {
        client.updateOrderStatus(notification, notification.getPaymentId());

    }

    @Async
    public void createRefundTransaction(TransactionNotification notification) {
        client.createRefundTransaction(notification, notification.getPaymentId());
    }
}
