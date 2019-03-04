package fun.hercules.user.enterprise.repository;

import fun.hercules.user.enterprise.domain.EnterpriseQualificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnterpriseQualificationHistoryRepository
        extends JpaRepository<EnterpriseQualificationHistory, String> {

    List<EnterpriseQualificationHistory> findByEnterpriseIdOrderByCreatedAtDesc(String enterpriseId);

    Optional<EnterpriseQualificationHistory> findTopByEnterpriseIdOrderByCreatedAtDesc(String enterpriseId);
}
