package fun.hercules.order.order.platform.order.service;

import fun.hercules.order.order.common.errors.ErrorCode;
import fun.hercules.order.order.common.exceptions.BadRequestException;
import fun.hercules.order.order.common.exceptions.OrderTypeNotFound;
import fun.hercules.order.order.platform.exports.BusinessOrder;
import fun.hercules.order.order.platform.exports.BusinessOrderService;
import fun.hercules.order.order.platform.exports.BusinessOrderType;
import fun.hercules.order.order.platform.exports.BusinessType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class BusinessOrderHandlers {
    private Map<String, BusinessHandler> orderHandlerMappings;

    private Map<String, BusinessHandler> orderPrefixHandlerMappings;

    public BusinessOrderHandlers(List<BusinessOrderService> businessOrderServices) {
        orderHandlerMappings = new HashMap<>();
        orderPrefixHandlerMappings = new HashMap<>();
        createOrderServiceMappings(businessOrderServices);
    }

    private void createOrderServiceMappings(List<BusinessOrderService> businessOrderServices) {
        businessOrderServices.forEach(service -> {
                    Optional.ofNullable(getServiceClass(service).getDeclaredAnnotation(BusinessOrderType.class))
                            .ifPresent(annotation ->
                            {
                                Class<? extends BusinessOrder> modelType = annotation.value();
                                BusinessType businessType = modelType.getDeclaredAnnotation(BusinessType.class);
                                BusinessHandler handler = new BusinessHandler(service, modelType);
                                log.info("create business handler for {}, type: {}, prefix: {}",
                                        modelType.getSimpleName(), businessType.value(), businessType.prefix());

                                orderHandlerMappings.put(businessType.value(), handler);
                                orderPrefixHandlerMappings.put(businessType.prefix(), handler);
                            });
                }
        );
    }

    private Class<?> getServiceClass(BusinessOrderService service) {
        if (AopUtils.isAopProxy(service)) {
            return AopUtils.getTargetClass(service);
        } else {
            return service.getClass();
        }
    }

    public BusinessHandler getByType(String type) {
        return Optional.ofNullable(orderHandlerMappings.get(type.toLowerCase()))
                .orElseThrow(() -> new OrderTypeNotFound(
                        ErrorCode.ORDER_TYPE_NOT_FOUND,
                        String.format("order type %s not found", type))
                );
    }

    public BusinessHandler getByOrderId(String orderId) {
        if (StringUtils.isEmpty(orderId)) {
            throw new BadRequestException(ErrorCode.INVALID_ORDER_ID,
                    String.format("order id %s is invalid", orderId));
        }
        String prefix = orderId.substring(0, 1);
        return Optional.ofNullable(orderPrefixHandlerMappings.get(prefix))
                .orElseThrow(() -> new OrderTypeNotFound(
                        ErrorCode.ORDER_TYPE_NOT_FOUND,
                        String.format("order prefix %s not found", prefix))
                );
    }
}
