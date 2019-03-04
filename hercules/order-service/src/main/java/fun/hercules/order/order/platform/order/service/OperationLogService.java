package fun.hercules.order.order.platform.order.service;

import com.google.common.collect.ImmutableList;
import fun.hercules.order.order.clients.CachedUserClient;
import fun.hercules.order.order.platform.exports.BusinessOrder;
import fun.hercules.order.order.platform.exports.BusinessOrderBase;
import fun.hercules.order.order.platform.order.model.OperationLog;
import fun.hercules.order.order.platform.order.model.OperationType;
import fun.hercules.order.order.platform.order.repository.OperationLogRepository;
import fun.hercules.order.order.platform.order.specifications.OperationLogSpecification;
import fun.hercules.order.order.platform.user.CurrentUser;
import fun.hercules.order.order.platform.user.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OperationLogService {
    private static final Set<String> VALID_USER_ROLES = ImmutableList.of(Role.EnterpriseUser, Role.PlatformAdmin, Role.PlatformAdmin)
            .stream()
            .map(role -> role.name())
            .collect(Collectors.toSet());

    private final CurrentUser currentUser;

    private final OperationLogRepository repository;

    private final CachedUserClient cachedUserClient;

    public OperationLogService(CurrentUser currentUser,
                               OperationLogRepository repository,
                               CachedUserClient cachedUserClient) {
        this.currentUser = currentUser;
        this.repository = repository;
        this.cachedUserClient = cachedUserClient;
    }

    public void paymentLog(BusinessOrder order, OperationType.Type type, String payUserId) {
        OperationLog operationLog = OperationLog.builder()
                .operation(OperationType.of(type))
                .operator(payUserId)
                .operatorRole(cachedUserClient.getUserRole(payUserId))
                .orderId(order.getId())
                .status(order.getStatus())
                .vendor(order.getVendor())
                .build();
        repository.save(operationLog);
        log.info("payment log operation {}", operationLog);
    }

    public void log(BusinessOrder order, OperationType.Type type) {
        String userId = getOperator(order);
        OperationLog operationLog = OperationLog.builder()
                .operation(OperationType.of(type))
                .operator(userId)
                .operatorRole(cachedUserClient.getUserRole(userId))
                .orderId(order.getId())
                .status(order.getStatus())
                .vendor(order.getVendor())
                .build();
        repository.save(operationLog);
        log.info("log operation {}", operationLog);
    }

    public String getOperator(BusinessOrder order) {
        if (VALID_USER_ROLES.contains(currentUser.getRole()) && !StringUtils.isEmpty(currentUser.getUserId())) {
            return currentUser.getUserId();
        } else {
            return order.getUserId();
        }
    }

    public BusinessOrder loadOperatorName(BusinessOrder businessOrder) {
        if (businessOrder instanceof BusinessOrderBase) {
            BusinessOrderBase order = (BusinessOrderBase) businessOrder;
            if (CollectionUtils.isEmpty(order.getOperationLogs())) {
                return businessOrder;
            }
            order.getOperationLogs().stream()
                    .filter(opLog -> !StringUtils.isEmpty(opLog.getOperator()))
                    .forEach(opLog -> opLog.setOperatorName(cachedUserClient.getUserNameById(opLog.getOperator())));
        }
        return businessOrder;
    }

    @PreAuthorize("hasAnyAuthority('PlatformAdmin')")
    public Page<OperationLog> list(Pageable pageable, Map<String, String> queries) {
        Page<OperationLog> operationLogs = repository.findAll(new OperationLogSpecification(queries), pageable);
        final int[] index = {1};
        return operationLogs.map(log -> {
            log.setIndex(pageable.getPageSize() * pageable.getPageNumber() + index[0]);
            index[0]++;
            log.setOperatorName(cachedUserClient.getUserNameById(log.getOperator()));
            return log;
        });
    }

}
