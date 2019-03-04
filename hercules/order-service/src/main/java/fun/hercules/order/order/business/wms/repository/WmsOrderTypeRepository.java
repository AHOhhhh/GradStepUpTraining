package fun.hercules.order.order.business.wms.repository;

import fun.hercules.order.order.business.wms.domain.WmsOrderType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WmsOrderTypeRepository extends JpaRepository<WmsOrderType, Integer> {
}
