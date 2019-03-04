package fun.hercules.order.order.business.wms.service;

import com.google.common.collect.Lists;
import fun.hercules.order.order.business.wms.domain.WmsOrderType;
import fun.hercules.order.order.business.wms.repository.WmsOrderTypeRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class WmsOrderTypeService {

    private final WmsOrderTypeRepository repository;

    public WmsOrderTypeService(WmsOrderTypeRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void onLoad() {
        Lists.reverse(Arrays.stream(WmsOrderType.Type.values()).collect(Collectors.toList())).forEach(type -> {
            repository.save(new WmsOrderType(type));
        });
    }
}
