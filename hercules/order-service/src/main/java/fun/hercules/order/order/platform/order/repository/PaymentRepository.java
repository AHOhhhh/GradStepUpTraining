package fun.hercules.order.order.platform.order.repository;

import fun.hercules.order.order.platform.order.model.Payment;
import fun.hercules.order.order.platform.order.model.PaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    List<Payment> findDistinctByPaymentRequestsInAndDeletedIsFalseOrderByCreatedAtDesc(List<PaymentRequest> paymentRequests);

    List<Payment> findDistinctByPaymentRequestsIdInAndDeletedIsFalse(List<String> paymentRequestIds);
}
