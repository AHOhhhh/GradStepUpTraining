package fun.hercules.order.order.platform.order.service;

import fun.hercules.order.order.clients.CachedUserClient;
import fun.hercules.order.order.platform.exports.BusinessOrder;
import fun.hercules.order.order.platform.exports.BusinessOrderBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Optional;

@Slf4j
@Service
public class BusinessOrderViewService {
    private final CachedUserClient cachedUserClient;

    public BusinessOrderViewService(CachedUserClient cachedUserClient) {
        this.cachedUserClient = cachedUserClient;
    }

    public BusinessOrder loadUserInfo(BusinessOrder businessOrder) {
        if (businessOrder instanceof BusinessOrderBase) {
            BusinessOrderBase order = (BusinessOrderBase) businessOrder;
            if (CollectionUtils.isEmpty(order.getOperationLogs())) {
                return businessOrder;
            }
            Optional.ofNullable(order.getUserId()).ifPresent(userId ->
                    order.setUserName(cachedUserClient.getUserNameById(userId)));
            Optional.ofNullable(order.getEnterpriseId()).ifPresent(enterpriseId ->
                    order.setEnterpriseName(cachedUserClient.getEnterpriseNameById(enterpriseId)));
        }
        return businessOrder;
    }
}
