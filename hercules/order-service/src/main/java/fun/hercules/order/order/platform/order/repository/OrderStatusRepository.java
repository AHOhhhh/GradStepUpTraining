package fun.hercules.order.order.platform.order.repository;

import fun.hercules.order.order.platform.order.model.OrderStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends CrudRepository<OrderStatus, Integer> {
}
