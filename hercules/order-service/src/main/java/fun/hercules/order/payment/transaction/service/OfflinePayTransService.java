package fun.hercules.order.payment.transaction.service;

import fun.hercules.order.order.common.errors.ErrorCode;
import fun.hercules.order.order.common.exceptions.AuthorizationException;
import fun.hercules.order.order.common.exceptions.BadRequestException;
import fun.hercules.order.order.common.exceptions.NotFoundException;
import fun.hercules.order.payment.transaction.domain.OfflinePayTrans;
import fun.hercules.order.payment.transaction.domain.PayStatus;
import fun.hercules.order.payment.transaction.repository.OfflineTransRepository;
import fun.hercules.order.payment.transaction.request.OfflineNotifyRequest;
import fun.hercules.order.payment.transaction.request.OfflineOrderInfo;
import fun.hercules.order.payment.transaction.response.PaymentNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static fun.hercules.order.order.common.errors.ErrorCode.COMMENT_NOT_EXISTED;
import static fun.hercules.order.order.common.errors.ErrorCode.OFFLINE_PAY_TRANSACTION_NOT_FOUND;

@Service
public class OfflinePayTransService {

    private final OfflineTransRepository repository;
    private Function<OfflineOrderInfo, OfflinePayTrans> converter;
    private BusinessOrderService businessOrderService;

    @Autowired
    public OfflinePayTransService(OfflineTransRepository repository,
                                  @Qualifier("offlineOrderInfoConverter") Function<OfflineOrderInfo, OfflinePayTrans> converter,
                                  BusinessOrderService businessOrderService) {
        this.repository = repository;
        this.converter = converter;
        this.businessOrderService = businessOrderService;
    }

    public void createOfflineTrans(OfflineOrderInfo offlineOrderInfo) {
        checkIsHavePaid(offlineOrderInfo.getPaymentId());
        repository.save(converter.apply(offlineOrderInfo));
    }

    public void updateTransStatus(OfflineNotifyRequest request) {
        OfflinePayTrans trans = repository.findById(request.getTransactionId()).orElseThrow(() -> new NotFoundException(OFFLINE_PAY_TRANSACTION_NOT_FOUND));
        if (request.isConfirmed()) {
            trans.setPayStatus(PayStatus.Success);
            trans.setPaidTime(Instant.now());
        } else {
            if (StringUtils.isEmpty(request.getComment())) {
                throw new BadRequestException(COMMENT_NOT_EXISTED);
            }
            trans.setPayStatus(PayStatus.Fail);
        }
        trans.setComment(request.getComment());
        repository.save(trans);

        businessOrderService.updateOrderPaymentStatus(PaymentNotification.builder().status(trans.getPayStatus()).paidTime(trans.getPaidTime())
                .paymentId(trans.getPaymentId()).paymentUserId(trans.getPayCustId()).operatorId(trans.getUpdatedBy()).build());
    }

    public OfflinePayTrans findOneByPaymentId(String paymentId) {
        return repository.findDistinctTopByPaymentIdOrderByUpdatedAtDesc(paymentId).orElseThrow(() -> new NotFoundException(OFFLINE_PAY_TRANSACTION_NOT_FOUND));
    }

    public List<OfflinePayTrans> findByPaymentId(String paymentId) {
        return repository.findByPaymentIdOrderByCreatedAtDesc(paymentId);
    }

    private void checkIsHavePaid(String paymentId) {
        List<OfflinePayTrans> offlinePayTransList = repository.findByPaymentId(paymentId);
        offlinePayTransList.forEach(t -> {
                    if (t.getPayStatus() == PayStatus.Success) {
                        throw new AuthorizationException(ErrorCode.NO_AUTHORIZED_ERROR, "already paid");
                    } else if (t.getPayStatus() == PayStatus.PayInProcess) {
                        throw new AuthorizationException(ErrorCode.NO_AUTHORIZED_ERROR, "wait for auditing");
                    }
                }
        );
    }
}
