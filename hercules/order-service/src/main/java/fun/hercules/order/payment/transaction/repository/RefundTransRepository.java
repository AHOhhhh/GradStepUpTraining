package fun.hercules.order.payment.transaction.repository;

import fun.hercules.order.payment.transaction.domain.RefundTrans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefundTransRepository extends JpaRepository<RefundTrans, String> {
    List<RefundTrans> findByPaymentIdOrderByCreatedAtDesc(String paymentId);
}
