package fun.hercules.user.enterprise.specification;

import fun.hercules.user.enterprise.domain.Enterprise;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 用于JPA做复杂查询
 */
public class EnterpriseSpecification implements Specification<Enterprise> {

    private SearchCriteria criteria;

    public EnterpriseSpecification(SearchCriteria searchCriteria) {
        this.criteria = searchCriteria;
    }

    // 根据不同的操作符，生成不同的predicate，用于JPA查询
    @Override
    public Predicate toPredicate(Root<Enterprise> root, CriteriaQuery<?>
            query, CriteriaBuilder cb) {
        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return cb.greaterThanOrEqualTo(
                    root.<String>get(criteria.getKey()), criteria.getValue()
                            .toString());
        } else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return cb.lessThanOrEqualTo(
                    root.<String>get(criteria.getKey()), criteria.getValue()
                            .toString());
        } else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return cb.like(
                        root.<String>get(criteria.getKey()), "%" + criteria
                                .getValue() + "%");
            } else {
                return cb.equal(root.get(criteria.getKey()), criteria
                        .getValue());
            }
        }
        return null;
    }
}
