package fun.hercules.order.order.platform.order.web;

import fun.hercules.order.order.platform.order.model.OperationLog;
import fun.hercules.order.order.platform.order.service.OperationLogService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/order-operations", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "/order-operations", description = "")
public class OperationLogController {

    private final OperationLogService operationLogService;

    public OperationLogController(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    @GetMapping
    public Page<OperationLog> getOrders(@SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                        @RequestParam MultiValueMap<String, String> queries) {
        return operationLogService.list(pageable, queries.toSingleValueMap());
    }
}
