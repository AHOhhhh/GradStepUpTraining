package fun.hercules.order.payment.transaction.service;

import fun.hercules.order.payment.transaction.domain.CurrencyCode;
import fun.hercules.order.payment.transaction.domain.PayStatus;
import fun.hercules.order.payment.transaction.domain.PaymentMethod;
import fun.hercules.order.payment.transaction.domain.RefundTrans;
import fun.hercules.order.payment.transaction.domain.TransactionType;
import fun.hercules.order.payment.transaction.repository.RefundTransRepository;
import fun.hercules.order.payment.transaction.request.RefundOrderInfo;
import fun.hercules.order.payment.transaction.response.TransactionNotification;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.List;
import java.util.function.Function;

@Service
public class RefundTransService {

    private final RefundTransRepository refundTransRepository;
    private Function<RefundOrderInfo, RefundTrans> converter;
    private final BusinessOrderService businessOrderService;

    public RefundTransService(RefundTransRepository refundTransRepository,
                              @Qualifier("refundOrderInfoConverter") Function<RefundOrderInfo, RefundTrans> converter,
                              BusinessOrderService businessOrderService) {
        this.refundTransRepository = refundTransRepository;
        this.converter = converter;
        this.businessOrderService = businessOrderService;
    }

    public void createRefundTrans(RefundOrderInfo refundOrderInfo) {
        RefundTrans refundTrans = refundTransRepository.save(converter.apply(refundOrderInfo));
        CurrencyCode currencyCode = refundTrans.getCurrencyCode();

        TransactionNotification notification = new TransactionNotification(
                refundTrans.getId(),
                refundOrderInfo.getPaymentId(),
                refundOrderInfo.getPayAmount(),
                TransactionType.Income,
                PaymentMethod.OFFLINE,
                refundTrans.getPayCustId(),
                refundTrans.getPayCustName(),
                Currency.getInstance(currencyCode.getCode()),
                PayStatus.Success,
                refundTrans.getCreatedAt(),
                refundTrans.getComment());

        businessOrderService.createRefundTransaction(notification);
    }

    public List<RefundTrans> findByPaymentId(String paymentId) {
        return refundTransRepository.findByPaymentIdOrderByCreatedAtDesc(paymentId);
    }
}
