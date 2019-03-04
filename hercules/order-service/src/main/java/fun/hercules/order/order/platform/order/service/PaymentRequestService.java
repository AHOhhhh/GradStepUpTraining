package fun.hercules.order.order.platform.order.service;

import fun.hercules.order.order.platform.exports.BusinessOrder;
import fun.hercules.order.order.platform.exports.PayableBusinessOrder;
import fun.hercules.order.order.platform.order.model.PaymentRequest;
import fun.hercules.order.order.platform.order.model.PaymentStatus;
import fun.hercules.order.order.platform.order.repository.PaymentRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class PaymentRequestService {

    private final PaymentRequestRepository paymentRequestRepository;

    public PaymentRequestService(PaymentRequestRepository paymentRequestRepository) {
        this.paymentRequestRepository = paymentRequestRepository;
    }

    public void createAndSavePaymentRequest(PayableBusinessOrder payableBusinessOrder) {
        PaymentRequest paymentRequest = new PaymentRequest(payableBusinessOrder);
        save(paymentRequest);
    }

    public void save(PaymentRequest paymentRequest) {
        if (CollectionUtils.isEmpty(paymentRequestRepository.findByOrderId(paymentRequest.getOrderId()))) {
            paymentRequestRepository.save(paymentRequest);
        } else {
            log.warn(String.format("order %s is already associated with payment requests", paymentRequest.getOrderId()));
        }
    }

    public PaymentRequest findByOrderId(String orderId) {
        return paymentRequestRepository.findByOrderId(orderId).get(0);
    }

    public List<PaymentRequest> findAllPaymentRequestsByOrderId(String orderId) {
        return paymentRequestRepository.findByOrderId(orderId);
    }

    public List<PaymentRequest> findAllPaymentRequestsByOrderIds(List<String> orderIds) {
        return paymentRequestRepository.findByOrderIdIn(orderIds);
    }

    public List<PaymentRequest> findAllByPaymentRequestIds(List<String> paymentRequestIds) {
        return paymentRequestRepository.findByIdIn(paymentRequestIds);
    }

    public void setSuccessStatus(BusinessOrder order) {
        PaymentRequest paymentRequest = findByOrderId(order.getId());
        paymentRequest.setPaymentStatus(PaymentStatus.Success);
        save(paymentRequest);
    }
}
