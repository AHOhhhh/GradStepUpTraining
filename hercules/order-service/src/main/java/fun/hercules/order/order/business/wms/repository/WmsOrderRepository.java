package fun.hercules.order.order.business.wms.repository;

import fun.hercules.order.order.business.wms.domain.WmsOrder;
import fun.hercules.order.order.business.wms.domain.WmsOrderType;
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
public interface WmsOrderRepository extends JpaRepository<WmsOrder, String>, JpaSpecificationExecutor<WmsOrder> {
    List<WmsOrder> findAllByEnterpriseIdAndTypeIsAndStatusIsNot(String enterpriseId, WmsOrderType wmsOrderType, OrderStatus status);

    Optional<WmsOrder> findOneById(String orderId);

    List<WmsOrder> findAllByIdIn(Collection<String> orderIds);

    Page<WmsOrder> findAllByEnterpriseId(String enterpriseId, Pageable pageable);

    Page<WmsOrder> findAllByEnterpriseIdAndStatusIn(String enterpriseId, List<OrderStatus> orderStatusList, Pageable pageable);
}
