package fun.hercules.order.payment.transaction.repository;

import fun.hercules.order.payment.transaction.domain.OfflinePayTrans;
import fun.hercules.order.payment.transaction.domain.PayStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfflineTransRepository extends JpaRepository<OfflinePayTrans, String> {
    List<OfflinePayTrans> findByPaymentId(String paymentId);

    Optional<OfflinePayTrans> findDistinctTopByPaymentIdOrderByUpdatedAtDesc(String paymentId);

    List<OfflinePayTrans> findByPaymentIdAndPayStatusNotOrderByCreatedAtDesc(String paymentId, PayStatus payStatus);

    List<OfflinePayTrans> findByPaymentIdOrderByCreatedAtDesc(String paymentId);
}
