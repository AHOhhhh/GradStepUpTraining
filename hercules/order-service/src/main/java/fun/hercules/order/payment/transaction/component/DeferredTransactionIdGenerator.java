package fun.hercules.order.payment.transaction.component;

import fun.hercules.order.payment.transaction.domain.DeferredPayTrans;

public class DeferredTransactionIdGenerator extends TransactionIdGenerator {
    @Override
    public String getUserIdHashCode(Object object) {
        DeferredPayTrans trans = (DeferredPayTrans) object;
        int userSuffix = Math.abs(trans.getPayCustId().hashCode() % 1000);
        return String.format("%03d", userSuffix);
    }
}
