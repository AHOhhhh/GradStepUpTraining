package fun.hercules.order.order.platform.order.repository;

import fun.hercules.order.order.platform.order.model.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTypeRepository extends JpaRepository<OrderType, Integer> {
}
