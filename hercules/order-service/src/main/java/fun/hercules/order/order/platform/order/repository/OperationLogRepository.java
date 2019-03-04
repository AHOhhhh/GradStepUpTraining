package fun.hercules.order.order.platform.order.repository;

import fun.hercules.order.order.platform.order.model.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long>, JpaSpecificationExecutor<OperationLog> {
    List<OperationLog> findAllByOrderIdOrderByCreatedAtDesc(String orderId);
}
