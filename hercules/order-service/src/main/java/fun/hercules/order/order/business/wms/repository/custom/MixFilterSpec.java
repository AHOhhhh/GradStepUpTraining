package fun.hercules.order.order.business.wms.repository.custom;

import fun.hercules.order.order.business.acg.domain.AcgOrder;
import fun.hercules.order.order.business.common.MixFilterBase;
import fun.hercules.order.order.business.wms.domain.WmsOrder;
import fun.hercules.order.order.platform.order.model.OrderStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class MixFilterSpec extends MixFilterBase<WmsOrder> implements Specification<WmsOrder> {
    public MixFilterSpec(String enterpriseId, String orderStatus) {
        super(enterpriseId, orderStatus);
    }

    @Override
    public Predicate toPredicate(Root<WmsOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return super.toBasePredicate(root, query, cb);
    }
}
