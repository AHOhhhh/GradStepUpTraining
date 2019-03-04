package fun.hercules.order.order.platform.exports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fun.hercules.order.order.platform.order.dto.OrderPageResponse;
import fun.hercules.order.order.platform.order.model.CancelReason;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BusinessOrderService<T extends BusinessOrder> {

    Optional<T> find(String orderId);

    T update(T order);

    T pay(String orderId, String paymentRequestIds);

    Optional<T> delete(String orderId);

    T create(T order);

    List<T> findAll(Set<String> orderIds);

    T cancel(String orderId, CancelReason cancelReason);

    @JsonIgnore
    default StatusTransitions getStatusTransitions() {
        return StatusTransitions.EMPTY;
    }

    OrderPageResponse<T> listByEnterpriseAndStatuses(String enterpriseId, String status, PageRequest pageRequest);
}
