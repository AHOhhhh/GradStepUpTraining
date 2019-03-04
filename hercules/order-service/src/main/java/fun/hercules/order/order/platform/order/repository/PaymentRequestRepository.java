package fun.hercules.order.order.platform.order.repository;

import fun.hercules.order.order.platform.order.model.PaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRequestRepository extends JpaRepository<PaymentRequest, String> {

    List<PaymentRequest> findByOrderId(String orderId);

    List<PaymentRequest> findByOrderIdIn(List<String> orderIds);

    List<PaymentRequest> findByIdIn(List<String> ids);
}
