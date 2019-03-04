package fun.hercules.order.order.platform.order.specifications;

import com.google.common.collect.ImmutableList;
import fun.hercules.order.order.platform.order.model.OperationLog;
import fun.hercules.order.order.platform.order.model.OperationType;
import fun.hercules.order.order.platform.user.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OperationLogSpecification implements Specification<OperationLog> {
    private Map<String, String> queries;

    public OperationLogSpecification(Map<String, String> queries) {
        this.queries = queries;
    }

    @Override
    public Predicate toPredicate(Root<OperationLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicate = getPredicate(root, cb, queries);
        Predicate[] pre = new Predicate[predicate.size()];
        return query.distinct(true).where(predicate.toArray(pre)).getRestriction();
    }

    private List<Predicate> getPredicate(Root<OperationLog> root, CriteriaBuilder cb, Map<String, String> queries) {
        List<Predicate> predicate = new ArrayList<>();
        if (StringUtils.isNotBlank(queries.get("fromDate"))) {
            predicate.add(cb.greaterThanOrEqualTo(root.get("createdAt"), LocalDate.parse(queries.get("fromDate")).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        }
        if (StringUtils.isNotBlank(queries.get("toDate"))) {
            predicate.add(cb.lessThanOrEqualTo(root.get("createdAt"), LocalDate.parse(queries.get("toDate")).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()));
        }
        if (StringUtils.isNotBlank(queries.get("orderId"))) {
            predicate.add(cb.equal(root.get("orderId").as(String.class), queries.get("orderId")));
        }
        predicate.add(root.get("operation").in(ImmutableList.of(
                OperationType.of(OperationType.Type.OfflinePaidConfirmFail),
                OperationType.of(OperationType.Type.OfflinePaidConfirmSuccess),
                OperationType.of(OperationType.Type.Audited),
                OperationType.of(OperationType.Type.Refunded))));
        predicate.add(cb.equal(root.get("operatorRole").as(String.class), Role.PlatformAdmin.name()));
        return predicate;
    }

}
