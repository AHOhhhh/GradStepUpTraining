package fun.hercules.order.payment.transaction.repository;

import fun.hercules.order.payment.transaction.domain.PayChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayChannelRepository extends JpaRepository<PayChannel, Integer> {

    Optional<PayChannel> findByValue(String value);
}
