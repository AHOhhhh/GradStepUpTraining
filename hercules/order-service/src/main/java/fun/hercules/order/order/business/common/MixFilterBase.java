package fun.hercules.order.order.business.common;

import fun.hercules.order.order.platform.exports.BusinessOrder;
import fun.hercules.order.order.platform.order.model.OrderStatus;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class MixFilterBase<T extends BusinessOrder> {
    private final String enterpriseId;
    private final String orderStatus;

    public MixFilterBase(String enterpriseId, String orderStatus) {
        this.enterpriseId = enterpriseId;
        this.orderStatus = orderStatus;
    }

    public Predicate toBasePredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Expression<OrderStatus> status = root.get("status");
        Path<String> enterpriseId = root.get("enterpriseId");

        Predicate predicate = cb.conjunction();

        if (!StringUtils.isEmpty(this.enterpriseId)) {
            predicate = cb.and(predicate, cb.equal(enterpriseId, this.enterpriseId));
        }
        List<OrderStatus> orderStatusList = new ArrayList<>();
        switch (this.orderStatus) {
            case "ToBeConfirmed":
                orderStatusList = getToBeConfirmedOrderStatuses();
                break;
            case "InProgress":
                orderStatusList.add(OrderStatus.of(OrderStatus.Type.OrderTracking));
                break;
            case "Completed":
                orderStatusList.add(OrderStatus.of(OrderStatus.Type.Closed));
                break;
            case "Cancelled":
                orderStatusList.add(OrderStatus.of(OrderStatus.Type.Cancelled));
                break;
            default:
                break;
        }
        if (orderStatusList.size() != 0) {
            predicate = cb.and(predicate, status.in(orderStatusList));
        }
        return predicate;
    }

    private List<OrderStatus> getToBeConfirmedOrderStatuses() {
        List<OrderStatus> orderStatusList = new ArrayList<>();
        orderStatusList.add(OrderStatus.of(OrderStatus.Type.Audited));
        orderStatusList.add(OrderStatus.of(OrderStatus.Type.ConfirmingPayment));
        orderStatusList.add(OrderStatus.of(OrderStatus.Type.OfflinePaidAwaitingConfirm));
        orderStatusList.add(OrderStatus.of(OrderStatus.Type.OfflinePaidNotConfirmed));
        orderStatusList.add(OrderStatus.of(OrderStatus.Type.Paid));
        orderStatusList.add(OrderStatus.of(OrderStatus.Type.Submitted));
        orderStatusList.add(OrderStatus.of(OrderStatus.Type.WaitForPay));
        return orderStatusList;
    }
}
