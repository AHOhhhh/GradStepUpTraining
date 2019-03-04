package fun.hercules.order.order.platform.order.service;

import com.google.common.collect.Lists;
import fun.hercules.order.order.platform.order.model.CancelReason;
import fun.hercules.order.order.platform.order.repository.CancelReasonRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class CancelReasonService {
    private final CancelReasonRepository repository;


    public CancelReasonService(CancelReasonRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void onLoad() {
        Lists.reverse(Arrays.stream(CancelReason.Type.values())
                .collect(Collectors.toList()))
                .forEach(type -> repository.save(CancelReason.of(type)));
    }
}
