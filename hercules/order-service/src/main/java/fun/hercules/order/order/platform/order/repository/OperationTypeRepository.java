package fun.hercules.order.order.platform.order.repository;

import fun.hercules.order.order.platform.order.model.OperationType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationTypeRepository extends CrudRepository<OperationType, Integer> {
}
