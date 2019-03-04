package fun.hercules.order.order.platform.order.repository;

import fun.hercules.order.order.platform.order.model.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentTransactionRepository extends
        JpaRepository<PaymentTransaction, String>, JpaSpecificationExecutor<PaymentTransaction> {
    List<PaymentTransaction> findByPaymentId(String paymentId);

    List<PaymentTransaction> findByTransactionId(String transactionId);

    List<PaymentTransaction> findByOrderIdIn(List<String> orderIds);
}
