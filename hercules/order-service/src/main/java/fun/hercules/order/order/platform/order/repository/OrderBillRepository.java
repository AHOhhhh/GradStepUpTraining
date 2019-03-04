package fun.hercules.order.order.platform.order.repository;

import fun.hercules.order.order.platform.order.model.OrderBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderBillRepository extends JpaRepository<OrderBill, Integer>, JpaSpecificationExecutor<OrderBill> {
    Page<OrderBill> findByOrderTypeInAndCreatedAtBetween(List<String> orderTypes, Instant from, Instant to, Pageable pageable);

    Page<OrderBill> findByOrderTypeIn(List<String> orderTypes, Pageable pageable);

    Optional<OrderBill> findByOrderId(String orderId);

    List<OrderBill> findByOrderIdInAndOrderTypeIn(Collection<String> orderIds, Collection<String> orderTypes);

    int countByOrderId(String orderId);
}
