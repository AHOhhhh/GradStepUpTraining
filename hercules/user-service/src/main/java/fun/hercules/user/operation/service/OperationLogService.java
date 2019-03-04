package fun.hercules.user.operation.service;


import fun.hercules.user.enterprise.domain.Enterprise;
import fun.hercules.user.operation.domain.OperationLog;
import fun.hercules.user.operation.domain.OperationType;
import fun.hercules.user.operation.repository.OperationLogRepository;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.security.CurrentUserContext;
import fun.hercules.user.enterprise.domain.Enterprise;
import fun.hercules.user.operation.repository.OperationLogRepository;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.security.CurrentUserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

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
import java.util.Optional;

@Slf4j
@Service
public class OperationLogService {

    private OperationLogRepository operationLogRepository;

    private CurrentUserContext loggedInUser;


    public OperationLogService(OperationLogRepository operationLogRepository, CurrentUserContext loggedInUser) {
        this.operationLogRepository = operationLogRepository;
        this.loggedInUser = loggedInUser;
    }

    /**
     * 保存不包含user
     *
     * @param enterprise 企业信息，主要用来获取企业Id和企业名称
     * @param type       操作类型
     */
    @PreAuthorize("hasAnyAuthority('PlatformAdmin', 'EnterpriseAdmin')")
    public void save(Enterprise enterprise, OperationType type) {
        OperationLog operationLog = build(enterprise, type, null);
        operationLogRepository.save(operationLog);
    }

    /**
     * 保存包含user
     */
    @PreAuthorize("hasAnyAuthority('EnterpriseAdmin')")
    public void saveWithUser(Enterprise enterprise, User user, OperationType type) {
        OperationLog operationLog = build(enterprise, type, user);
        operationLogRepository.save(operationLog);
    }

    /**
     * 根据参数封装OperationLog对象
     *
     * @param enterprise 构建操作记录所需要的企业信息，主要用来获取企业ID和企业名称
     * @param type       操作类型
     * @param user       当前用户
     * @return 构建好的操作记录
     */
    private OperationLog build(Enterprise enterprise, OperationType type, User user) {
        String targetUserId = Optional.ofNullable(user).isPresent() ? user.getId() : "";
        String targetUserName = Optional.ofNullable(user).isPresent() ? user.getUsername() : "";

        return OperationLog.builder().enterpriseId(enterprise.getId()).enterpriseName(enterprise.getName())
                .operator(loggedInUser.getUser())
                .operatorRole(loggedInUser.getUser().getRole().getName())
                .targetUserId(targetUserId)
                .targetUserName(targetUserName)
                .type(type)
                .build();
    }

    /**
     * 根据不同查询条件，构建不同的specification，并检索出来，返回分页数据
     *
     * @param pageable 分页信息
     * @param queries  查询条件,包括开始日期，结束日期，企业名称
     * @return 查询到的操作记录列表
     */
    @PreAuthorize("hasAnyAuthority('PlatformAdmin')")
    public Page<OperationLog> list(Pageable pageable, Map<String, String> queries) {
        Page<OperationLog> operationLogs = operationLogRepository.findAll(new OperationLogSpecification(queries), pageable);
        final int[] index = {1};
        return operationLogs.map(operationLog -> {
            operationLog.setIndex(pageable.getPageSize() * pageable.getPageNumber() + index[0]);
            index[0]++;
            String fullname = StringUtils.isNotBlank(operationLog.getOperator().getFullname())
                    ? operationLog.getOperator().getFullname() : operationLog.getOperator().getUsername();
            operationLog.setOperatorName(fullname);
            operationLog.setTypeDescription(operationLog.getType().getDescription());
            return operationLog;
        });
    }

    /**
     * 根据查询语句，生成出JPA可用的specification，用于多条件查询
     */
    private static class OperationLogSpecification implements Specification<OperationLog> {
        private Map<String, String> queries;

        public OperationLogSpecification(Map<String, String> queries) {
            this.queries = queries;
        }

        @Override
        public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
            List<Predicate> predicate = getPredicate(root, cb, queries);
            Predicate[] pre = new Predicate[predicate.size()];
            return query.distinct(true).where(predicate.toArray(pre)).getRestriction();
        }

        /**
         * 多条件生成多条Predicate
         *
         * @param root    查询表达式，此处用来对OperationLog.class进行计算的表达式
         * @param cb      CriteriaBuilder
         * @param queries 查询条件
         * @return Predicate列表，Predicate的计算为布尔值true或false
         */
        private List<Predicate> getPredicate(Root<OperationLog> root, CriteriaBuilder cb, Map<String, String> queries) {
            List<Predicate> predicate = new ArrayList<>();
            if (StringUtils.isNotBlank(queries.get("role"))) {
                predicate.add(cb.equal(root.get("operatorRole"), queries.get("role")));
            }
            if (StringUtils.isNotBlank(queries.get("fromDate"))) {
                predicate.add(cb.greaterThanOrEqualTo(root.get("createdAt"), LocalDate.parse(queries.get("fromDate")).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            }
            if (StringUtils.isNotBlank(queries.get("toDate"))) {
                predicate.add(cb.lessThanOrEqualTo(root.get("createdAt"), LocalDate.parse(queries.get("toDate")).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()));
            }
            if (StringUtils.isNotBlank(queries.get("enterpriseName"))) {
                predicate.add(cb.like(root.get("enterpriseName").as(String.class), "%" + queries.get("enterpriseName") + "%"));
            }
            if (StringUtils.isNotBlank(queries.get("enterpriseId"))) {
                predicate.add(cb.equal(root.get("enterpriseId"), queries.get("enterpriseId")));
            }
            return predicate;
        }
    }

}
