package fun.hercules.order.order.platform.order.specifications;

import fun.hercules.order.order.platform.order.model.OrderType;
import fun.hercules.order.order.platform.order.model.PaymentStatus;
import fun.hercules.order.order.platform.order.model.PaymentTransaction;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.List;

public class PaymentTransactionSpecs {

    public static Specification<PaymentTransaction> inTheRangeOfPaidTime(Range<Instant> paidTimeRange) {
        return (root, query, cb) -> cb.between(root.get("paidTime"), paidTimeRange.getLowerBound().getValue().orElse(null), paidTimeRange.getUpperBound().getValue().orElse(null));
    }

    public static Specification<PaymentTransaction> matchTheEnterpriseIds(List<String> enterpriseIds) {
        return (root, query, cb) -> root.get("enterpriseId").in(enterpriseIds);
    }

    public static Specification<PaymentTransaction> equalWithOrderId(String orderId) {
        return (root, query, cb) -> cb.equal(root.get("orderId"), orderId);
    }

    public static Specification<PaymentTransaction> matchThePaymentStatus(List<PaymentStatus> statuses) {
        return (root, query, cb) -> root.get("status").in(statuses);
    }

    public static Specification<PaymentTransaction> matchTheOrderTypes(List<OrderType> orderTypes) {
        return (root, query, cb) -> root.get("orderType").in(orderTypes);
    }
}
