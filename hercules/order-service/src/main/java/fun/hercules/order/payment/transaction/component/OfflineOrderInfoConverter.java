package fun.hercules.order.payment.transaction.component;

import fun.hercules.order.order.common.exceptions.NotFoundException;
import fun.hercules.order.payment.transaction.domain.CurrencyCode;
import fun.hercules.order.payment.transaction.domain.OfflinePayTrans;
import fun.hercules.order.payment.transaction.repository.CurrencyCodeRepository;
import fun.hercules.order.payment.transaction.request.OfflineOrderInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static fun.hercules.order.order.common.errors.ErrorCode.CURRENCY_CODE_NOT_FOUND;


@Component
public class OfflineOrderInfoConverter implements Function<OfflineOrderInfo, OfflinePayTrans> {
    private static final String[] errorSoon = {"id", "payStatus", "paidTime"};
    private final CurrencyCodeRepository currencyCodeRepository;

    @Autowired
    public OfflineOrderInfoConverter(CurrencyCodeRepository currencyCodeRepository) {
        this.currencyCodeRepository = currencyCodeRepository;
    }

    @Override
    public OfflinePayTrans apply(OfflineOrderInfo offlineOrderInfo) {
        CurrencyCode currencyCode = currencyCodeRepository.findByCode(offlineOrderInfo.getCurrencyCode()).orElseThrow(() -> new NotFoundException(CURRENCY_CODE_NOT_FOUND));
        OfflinePayTrans trans = OfflinePayTrans.builder().currencyCode(currencyCode).build();
        BeanUtils.copyProperties(offlineOrderInfo, trans, errorSoon);
        return trans;
    }
}
