package fun.hercules.order.order.platform.order.service;

import fun.hercules.order.order.platform.exports.BusinessOrder;
import fun.hercules.order.order.platform.exports.BusinessOrderBase;
import fun.hercules.order.order.platform.user.CurrentUser;
import fun.hercules.order.order.platform.user.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.function.Function;

@Slf4j
@Service
public class OrderRenderService {

    private final Function<BusinessOrder, BusinessOrder> renders;


    private final boolean renderPlatformService;

    private final CurrentUser currentUser;

    public OrderRenderService(OperationLogService operationLogService,
                              BusinessOrderViewService businessOrderViewService,
                              @Value("${order.render.platform-service.enabled}") boolean renderPlatformService,
                              CurrentUser currentUser) {
        this.renderPlatformService = renderPlatformService;
        this.currentUser = currentUser;

        log.info("platform service render enabled: {}", renderPlatformService);
        renders = registerRenders(
                operationLogService::loadOperatorName,
                businessOrderViewService::loadUserInfo
        );
    }

    private Function<BusinessOrder, BusinessOrder> registerRenders(Function<BusinessOrder, BusinessOrder>... renders) {
        return Arrays.stream(renders).reduce(Function.identity(), Function::andThen);
    }

    public BusinessOrder renderOrder(BusinessOrder businessOrder) {
        if (renderPlatformService || !isPlatformService()) {
            return renders.apply(businessOrder);
        } else {
            if (businessOrder instanceof BusinessOrderBase) {
                removeOperationLog((BusinessOrderBase) businessOrder);
            }
            return businessOrder;
        }
    }

    private void removeOperationLog(BusinessOrderBase order) {
        order.setOperationLogs(null);
    }

    private boolean isPlatformService() {
        return Role.PlatformService.name().equals(currentUser.getRole());
    }
}
