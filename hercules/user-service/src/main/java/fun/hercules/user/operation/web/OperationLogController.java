package fun.hercules.user.operation.web;

import fun.hercules.user.operation.domain.OperationLog;
import fun.hercules.user.operation.service.OperationLogService;
import fun.hercules.user.operation.domain.OperationLog;
import fun.hercules.user.operation.service.OperationLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OperationLogController {

    private final OperationLogService operationLogService;

    public OperationLogController(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    @GetMapping(value = "/user-operations")
    public Page<OperationLog> getOrders(@SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                        @RequestParam MultiValueMap<String, String> queries) {
        return operationLogService.list(pageable, queries.toSingleValueMap());
    }
}
