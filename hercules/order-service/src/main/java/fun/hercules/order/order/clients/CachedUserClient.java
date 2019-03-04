package fun.hercules.order.order.clients;

import fun.hercules.order.order.clients.dto.Enterprise;
import fun.hercules.order.order.clients.dto.User;
import fun.hercules.order.order.common.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@Component
public class CachedUserClient {
    private UserServiceClient serviceClient;

    public CachedUserClient(UserServiceClient userClient) {
        this.serviceClient = userClient;
    }

    @Cacheable(value = "users", unless = "#result == null")
    public Optional<User> getUserById(String userId) {
        try {
            Optional<User> user = Optional.ofNullable(serviceClient.getUserById(userId));
            return user;
        } catch (RuntimeException e) {
            log.warn(String.format("failed to load user %s", userId), e);
            return Optional.empty();
        }
    }

    @Cacheable(value = "enterprises", unless = "#result == null")
    public Optional<Enterprise> getEnterpriseById(String userId) {
        try {
            Optional<Enterprise> enterprise = Optional.ofNullable(serviceClient.getEnterpriseById(userId));
            return enterprise;
        } catch (RuntimeException e) {
            log.warn(String.format("failed to load enterprise %s", userId), e);
            return Optional.empty();
        }
    }

    public String getUserNameById(String userId) {
        Optional<User> user = getUser(userId);
        if (!user.isPresent()) {
            return userId;
        } else if (StringUtils.isEmpty(user.get().getFullname())) {
            return user.get().getUsername();
        } else {
            return user.get().getFullname();
        }
    }

    public String getUserRole(String userId) {
        return getUser(userId).map(User::getRole).orElse(null);
    }

    public Optional<User> getUser(String userId) {
        CachedUserClient cachedUserClient = (CachedUserClient) SpringContextUtil.getBean(this.getClass());
        if (null != cachedUserClient) {
            return cachedUserClient.getUserById(userId);
        } else {
            return getUserById(userId);
        }
    }

    public String getEnterpriseNameById(String enterpriseId) {
        Optional<Enterprise> enterprise = getEnterpriseById(enterpriseId);
        if (!enterprise.isPresent()) {
            return enterpriseId;
        } else
            return enterprise.get().getName();
    }
}
