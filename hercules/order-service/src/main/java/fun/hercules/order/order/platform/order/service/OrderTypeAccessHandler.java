package fun.hercules.order.order.platform.order.service;

import fun.hercules.order.order.common.exceptions.UnauthorizedException;
import fun.hercules.order.order.platform.user.CurrentUser;
import fun.hercules.order.order.platform.user.OrderTypeAccess;
import fun.hercules.order.order.platform.user.Privilege;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static fun.hercules.order.order.common.errors.ErrorCode.ORDER_ACCESS_DENIED;

@Slf4j
@Service
public class OrderTypeAccessHandler {

    private final CurrentUser currentUser;

    @Autowired
    public OrderTypeAccessHandler(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    public List<String> validateAndGetPermittedOrderTypes() {
        if (currentUser.getRole().equals("PlatformAdmin")) {
            return Arrays.asList("wms", "acg");
        }
        List<String> orderTypes = new ArrayList<>();
        currentUser.getPrivileges().forEach(p -> {
            try {
                OrderTypeAccess annotation = Privilege.Type.class.getDeclaredField(p.getType().name()).getAnnotation(OrderTypeAccess.class);
                if (Objects.isNull(annotation)) {
                    log.warn("unDeclaredAnnotation For OrderTypeAccess");
                } else {
                    orderTypes.addAll(Arrays.asList(annotation.value()));
                }
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        });

        if (orderTypes.isEmpty()) {
            log.warn("no order access for currentUser: {}", currentUser.getUserId());
            throw new UnauthorizedException(ORDER_ACCESS_DENIED);
        }
        return orderTypes;
    }
}
