package fun.hercules.order.order.utils;

import fun.hercules.order.order.platform.order.model.PaymentRequest;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentUtils {
    public static String joinPaymentRequestIds(List<PaymentRequest> payments) {
        List<String> paymentRequestIds = payments.stream()
                .map(paymentRequest -> paymentRequest.getId())
                .collect(Collectors.toList());
        return String.join(",", paymentRequestIds);
    }
}
