package fun.hercules.user.enterprise.specification;

import fun.hercules.user.common.constants.Status;
import fun.hercules.user.enterprise.domain.Enterprise;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.util.ArrayList;
import java.util.List;

public class EnterpriseSpecificationBuilder {

    private final List<SearchCriteria> params;

    public EnterpriseSpecificationBuilder() {
        this.params = new ArrayList<SearchCriteria>();
    }

    public EnterpriseSpecificationBuilder with(String key, String operation,
                                               Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    // 将多参数构建为符合JPA要求的条件，多条件之间为and关系
    public Specification<Enterprise> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<Enterprise>> specs = new ArrayList<>();

        for (SearchCriteria param : params) {
            specs.add(new EnterpriseSpecification(param));
        }

        Specification<Enterprise> result = Specifications.where(specs.get(0));
        for (int i = 1; i < specs.size(); i++) {
            result = Specifications.where(result).and(specs.get(i));
        }

        return result;
    }

    /**
     * 多条件查询，根据参数有值与否，拼出不同的参数组合，并调用build方法生成specification供JPA查询使用。
     *
     * @param name             企业名称
     * @param status           状态 （启用|禁用）
     * @param validationStatus 审核状态
     * @return 搜索企业的specification（JPA search用）
     */
    public Specification<Enterprise> buildWithParams(String name, String
            status, String validationStatus) {
        if (status != null) {
            if (status.equalsIgnoreCase("ENABLED")) {
                this.with("status", ":", Status.ENABLED);
            } else if (status.equalsIgnoreCase("DISABLED")) {
                this.with("status", ":", Status.DISABLED);
            }
        }

        if (name != null && !name.trim().isEmpty()) {
            this.with("name", ":", name);
        }

        if (validationStatus != null) {
            if (validationStatus.equalsIgnoreCase("AuthorizationInProcess")) {
                this.with("validationStatus", ":", Enterprise
                        .ValidationStatus.AuthorizationInProcess);
            } else if (validationStatus.equalsIgnoreCase("Authorized")) {
                this.with("validationStatus", ":", Enterprise
                        .ValidationStatus.Authorized);
            } else if (validationStatus.equalsIgnoreCase("Unauthorized")) {
                this.with("validationStatus", ":", Enterprise
                        .ValidationStatus.Unauthorized);
            }
        }

        return this.build();
    }
}
