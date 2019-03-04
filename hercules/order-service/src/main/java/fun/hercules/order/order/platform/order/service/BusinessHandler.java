package fun.hercules.order.order.platform.order.service;

import fun.hercules.order.order.common.errors.ErrorCode;
import fun.hercules.order.order.common.exceptions.BadRequestException;
import fun.hercules.order.order.platform.exports.BusinessOrder;
import fun.hercules.order.order.platform.exports.BusinessOrderService;
import fun.hercules.order.order.platform.exports.StatusTransitions;
import fun.hercules.order.order.platform.order.dto.OrderPageResponse;
import fun.hercules.order.order.platform.order.model.CancelReason;
import fun.hercules.order.order.utils.JsonParseException;
import fun.hercules.order.order.utils.JsonUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
class BusinessHandler {
    @Getter
    private final BusinessOrderService<BusinessOrder> service;

    @Getter
    private final Class<? extends BusinessOrder> modelType;

    private final OrderFieldsMapper fieldsMapper;

    public BusinessHandler(BusinessOrderService<BusinessOrder> service, Class<? extends BusinessOrder> modelType) {
        this.service = service;
        this.modelType = modelType;
        fieldsMapper = new OrderFieldsMapper(modelType);
    }

    public Optional<BusinessOrder> find(String orderId) {
        return service.find(orderId);
    }

    public Optional<BusinessOrder> delete(String orderId) {
        return service.delete(orderId);
    }

    public BusinessOrder create(BusinessOrder order) {
        return service.create(order);
    }


    public BusinessOrder update(BusinessOrder oldOrder, BusinessOrder newOrder) {
        return updateOrder(fieldsMapper.map(newOrder, oldOrder));
    }

    public BusinessOrder update(BusinessOrder updatedOrder) {
        return updateOrder(updatedOrder);
    }

    private BusinessOrder updateOrder(BusinessOrder updatedOrder) {
        return service.update(updatedOrder);
    }

    public BusinessOrder deserialize(String orderJson) {
        try {
            return JsonUtils.unmarshal(orderJson, modelType);
        } catch (JsonParseException e) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST, e.getMessage(), e);
        }
    }

    public List<BusinessOrder> findAll(Set<String> orderIds) {
        return service.findAll(orderIds);
    }

    public BusinessOrder pay(String orderId, String paymentRequestIds) {
        return service.pay(orderId, paymentRequestIds);
    }

    public BusinessOrder cancel(String orderId, CancelReason cancelReason) {
        return service.cancel(orderId, cancelReason);
    }

    public StatusTransitions getStatusTransitions() {
        return service.getStatusTransitions();
    }

    public OrderPageResponse<BusinessOrder> listAllByEnterpriseAndStatuses(String enterpriseId, String status, PageRequest pageRequest) {
        return service.listByEnterpriseAndStatuses(enterpriseId, status, pageRequest);
    }
}
