package fun.hercules.order.payment.transaction.component;

import fun.hercules.order.payment.transaction.domain.RefundTrans;

public class RefundTransactionIdGenerator extends TransactionIdGenerator {
    @Override
    public String getUserIdHashCode(Object object) {
        RefundTrans trans = (RefundTrans) object;
        int userSuffix = Math.abs(trans.getPayCustId().hashCode() % 1000);
        return String.format("%03d", userSuffix);
    }
}
