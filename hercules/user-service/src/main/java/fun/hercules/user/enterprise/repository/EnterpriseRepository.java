package fun.hercules.user.enterprise.repository;

import fun.hercules.user.enterprise.domain.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise, String>, JpaSpecificationExecutor<Enterprise> {
    Optional<Enterprise> findById(String enterpriseId);

    Optional<Enterprise> findByName(String name);
}
