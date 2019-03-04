package fun.hercules.user.enterprise.repository;

import fun.hercules.user.enterprise.domain.Notification;
import fun.hercules.user.enterprise.domain.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    Page<Notification> findByEnterpriseIdAndNotificationType(String enterpriseId, NotificationType notificationType, Pageable pageable);
}
