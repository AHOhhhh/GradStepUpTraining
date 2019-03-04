package fun.hercules.order.order.platform.order.validators;

import fun.hercules.order.order.platform.order.model.OrderBill;
import fun.hercules.order.order.platform.order.utils.ChargeStatusMapping;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Component
public class OrderBillImportationValidator {


    public OrderBillImportationValidator() {
    }

    public List<String> validate(List<Map<String, Object>> importationData,
                                 List<OrderBill> orderBillsInDB) {
        Map<String, OrderBill> orderBillMap = orderBillsInDB.stream().collect(toMap(OrderBill::getOrderId, Function.identity()));
        List<String> orderIds = importationData.stream()
                .filter(item -> orderBillMap.containsKey(String.valueOf(item.get("orderId"))))
                .filter(item -> isInvalidStatus(orderBillMap, item))
                .map(item -> String.valueOf(item.get("orderId")))
                .collect(toList());
        return orderIds;
    }

    private boolean isInvalidStatus(Map<String, OrderBill> orderBillMap, Map<String, Object> item) {
        OrderBill orderBill = orderBillMap.get(String.valueOf(item.get("orderId")));
        String productChargeStatus = String.valueOf(item.get("productChargeStatus"));
        String serviceChargeStatus = String.valueOf(item.get("serviceChargeStatus"));
        String commissionChargeStatus = String.valueOf(item.get("commissionChargeStatus"));
        return invalidProductChargeStatus(productChargeStatus)
                || invalidServiceChargeStatus(serviceChargeStatus, orderBill)
                || invalidCommissionChargeStatus(commissionChargeStatus, orderBill);
    }

    private boolean invalidProductChargeStatus(String status) {
        return Objects.isNull(ChargeStatusMapping.fromProductChargeStatus(status));
    }

    private boolean invalidServiceChargeStatus(String status, OrderBill orderBill) {
        return Objects.isNull(ChargeStatusMapping.fromServiceChargeStatus(status, orderBill.getPayChannel()));
    }

    private boolean invalidCommissionChargeStatus(String status, OrderBill orderBill) {
        return Objects.isNull(ChargeStatusMapping.fromCommissionChargeStatus(status, orderBill.getPayChannel()));
    }
}

