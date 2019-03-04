package fun.hercules.order.order.platform.order.service;

import com.google.common.collect.Lists;
import fun.hercules.order.order.platform.order.model.OperationType;
import fun.hercules.order.order.platform.order.repository.OperationTypeRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class OperationTypeService {
    private OperationTypeRepository repository;

    public OperationTypeService(OperationTypeRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void onLoad() {
        Lists.reverse(Arrays.stream(OperationType.Type.values()).collect(Collectors.toList())).forEach(type -> {
            repository.save(new OperationType(type));
        });
    }
}

