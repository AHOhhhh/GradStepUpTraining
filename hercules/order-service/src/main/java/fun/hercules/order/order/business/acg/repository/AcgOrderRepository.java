package fun.hercules.order.order.business.acg.repository;

import fun.hercules.order.order.business.acg.domain.AcgOrder;
import fun.hercules.order.order.platform.order.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AcgOrderRepository extends JpaRepository<AcgOrder, String>, JpaSpecificationExecutor<AcgOrder> {
    Optional<AcgOrder> findOneById(String orderId);

    List<AcgOrder> findAllByIdIn(Collection<String> ids);

    Page<AcgOrder> findAllByEnterpriseId(String enterpriseId, Pageable pageable);

    Page<AcgOrder> findAllByEnterpriseIdAndStatusIn(String enterpriseId, List<OrderStatus> orderStatusList, Pageable pageable);
}
