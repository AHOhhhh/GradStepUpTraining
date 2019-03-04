package fun.hercules.order.order.platform.order.repository;

import fun.hercules.order.order.platform.order.model.OrderLogistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLogisticRepository extends JpaRepository<OrderLogistic, Integer> {
}
