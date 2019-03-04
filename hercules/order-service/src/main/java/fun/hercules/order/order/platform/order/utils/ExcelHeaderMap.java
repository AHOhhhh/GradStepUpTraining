package fun.hercules.order.order.platform.order.utils;


import com.google.common.collect.ImmutableBiMap;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Sets.newHashSet;

public class ExcelHeaderMap {

    private static final ImmutableBiMap<String, String> headerMap = ImmutableBiMap.<String, String>builder()
            .put("orderId", "订单号")
            .put("orderType", "产品类型")
            .put("vendor", "服务商")
            .put("productCharge", "产品费金额")
            .put("serviceCharge", "服务费金额")
            .put("commissionCharge", "手续费金额")
            .put("productChargeStatus", "产品费状态")
            .put("serviceChargeStatus", "服务费状态")
            .put("commissionChargeStatus", "手续费状态")
            .put("createdAt", "创建时间")
            .put("payChannel", "支付对象")
            .put("payMethod", "支付方式")
            .put("currency", "货币类型")
            .build();

    private static final Set<String> CURRENCY_COLUMN = newHashSet("productCharge", "serviceCharge", "commissionCharge",
            "产品费金额", "服务费金额", "手续费金额");

    public static List<String> getHeaderValues(List<String> headerKeys) {
        return headerKeys.stream().map(key -> headerMap.get(key)).collect(Collectors.toList());
    }

    public static List<String> getHeaderKeys(List<String> headerValues) {
        return headerValues.stream().map(value -> headerMap.inverse().get(value)).collect(Collectors.toList());
    }

    public static boolean isCurrencyCell(String header) {
        return CURRENCY_COLUMN.contains(header);
    }
}
