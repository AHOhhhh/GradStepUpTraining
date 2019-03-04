package fun.hercules.order.order.platform.order.specifications;

import fun.hercules.order.order.platform.order.model.OrderBill;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.List;

public class OrderBillSpecs {

    public static Specification<OrderBill> createdAtBetween(Range<Instant> range) {
        return (root, query, cb) -> cb.between(root.get("createdAt"), range.getLowerBound().getValue().orElse(null), range.getUpperBound().getValue().orElse(null));
    }

    public static Specification<OrderBill> inOrderTypes(List<String> orderTypes) {
        return (root, query, cb) -> root.get("orderType").in(orderTypes);
    }

    public static Specification<OrderBill> equalWithOrderId(String orderId) {
        return (root, query, cb) -> cb.equal(root.get("orderId"), orderId);
    }
}
