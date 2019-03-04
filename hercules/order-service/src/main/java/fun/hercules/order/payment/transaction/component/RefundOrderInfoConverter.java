package fun.hercules.order.payment.transaction.component;

import fun.hercules.order.order.common.exceptions.NotFoundException;
import fun.hercules.order.payment.transaction.domain.CurrencyCode;
import fun.hercules.order.payment.transaction.domain.RefundTrans;
import fun.hercules.order.payment.transaction.repository.CurrencyCodeRepository;
import fun.hercules.order.payment.transaction.request.RefundOrderInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static fun.hercules.order.order.common.errors.ErrorCode.CURRENCY_CODE_NOT_FOUND;

@Component
public class RefundOrderInfoConverter implements Function<RefundOrderInfo, RefundTrans> {
    private static final String[] errorSoon = {"id", "payStatus", "paidTime"};
    private final CurrencyCodeRepository currencyCodeRepository;

    @Autowired
    public RefundOrderInfoConverter(CurrencyCodeRepository currencyCodeRepository) {
        this.currencyCodeRepository = currencyCodeRepository;
    }

    @Override
    public RefundTrans apply(RefundOrderInfo refundOrderInfo) {
        CurrencyCode currencyCode = currencyCodeRepository.findByCode(refundOrderInfo.getCurrencyCode()).orElseThrow(() -> new NotFoundException(CURRENCY_CODE_NOT_FOUND));
        RefundTrans trans = RefundTrans.builder().currencyCode(currencyCode).build();
        BeanUtils.copyProperties(refundOrderInfo, trans, errorSoon);
        return trans;
    }
}
