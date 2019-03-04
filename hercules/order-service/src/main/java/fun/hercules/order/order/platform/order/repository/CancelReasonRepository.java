package fun.hercules.order.order.platform.order.repository;

import fun.hercules.order.order.platform.order.model.CancelReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CancelReasonRepository extends JpaRepository<CancelReason, Integer> {
}
