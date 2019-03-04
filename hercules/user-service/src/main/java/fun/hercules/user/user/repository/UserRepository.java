package fun.hercules.user.user.repository;

import fun.hercules.user.common.constants.Status;
import fun.hercules.user.user.domain.Role;
import fun.hercules.user.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    Page<User> findByEnterpriseId(String enterpriseId, Pageable pageable);

    List<User> findByEnterpriseId(String enterpriseId);

    Optional<User> findByEnterpriseIdAndRole(String enterpriseId, Role role);

    List<User> findByEnterpriseIdIsInAndRole(List<String> enterpriseId, Role role);

    @Modifying
    @Query("update User set status = ?1  where enterpriseId = ?2")
    int updateStatus(Status status, String enterpriseId);

}
