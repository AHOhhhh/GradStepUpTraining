package fun.hercules.order.order.business.acg.repository.custom;

import fun.hercules.order.order.business.acg.domain.AcgOrder;
import fun.hercules.order.order.business.common.MixFilterBase;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class MixFilterSpec extends MixFilterBase<AcgOrder> implements Specification<AcgOrder> {

    public MixFilterSpec(String enterpriseId, String orderStatus) {
        super(enterpriseId, orderStatus);
    }

    @Override
    public Predicate toPredicate(Root<AcgOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return super.toBasePredicate(root, query, cb);
    }
}
