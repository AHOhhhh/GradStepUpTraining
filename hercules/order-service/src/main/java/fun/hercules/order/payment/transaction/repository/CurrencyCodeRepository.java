package fun.hercules.order.payment.transaction.repository;

import fun.hercules.order.payment.transaction.domain.CurrencyCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyCodeRepository extends JpaRepository<CurrencyCode, Integer> {

    Optional<CurrencyCode> findByCode(String code);
}
