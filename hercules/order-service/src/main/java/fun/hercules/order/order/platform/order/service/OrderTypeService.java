package fun.hercules.order.order.platform.order.service;

import com.google.common.collect.Lists;
import fun.hercules.order.order.platform.order.model.OrderType;
import fun.hercules.order.order.platform.order.repository.OrderTypeRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class OrderTypeService {

    private final OrderTypeRepository repository;

    public OrderTypeService(OrderTypeRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void onLoad() {
        Lists.reverse(Arrays.stream(OrderType.Type.values())
                .collect(Collectors.toList()))
                .forEach(type -> repository.save(OrderType.of(type)));
    }
}
