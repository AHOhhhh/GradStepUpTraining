package fun.hercules.order.order.platform.order.service;

import com.google.common.collect.Lists;
import fun.hercules.order.order.platform.order.model.OrderStatus;
import fun.hercules.order.order.platform.order.repository.OrderStatusRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class OrderStatusService {
    private OrderStatusRepository repository;

    public OrderStatusService(OrderStatusRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void onLoad() {
        Lists.reverse(Arrays.stream(OrderStatus.Type.values()).collect(Collectors.toList())).forEach(type -> {
            repository.save(new OrderStatus(type));
        });
    }
}

