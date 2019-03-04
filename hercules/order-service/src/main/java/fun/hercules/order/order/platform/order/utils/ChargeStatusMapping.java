package fun.hercules.order.order.platform.order.utils;

import com.google.common.collect.ImmutableBiMap;
import fun.hercules.order.order.platform.order.model.PayChannel;

public class ChargeStatusMapping {

    private static final ImmutableBiMap<Boolean, String> PRODUCT_CHARGE_STATUSES = ImmutableBiMap
            .<Boolean, String>builder()
            .put(Boolean.FALSE, "待收取")
            .put(Boolean.TRUE, "已收取")
            .build();

    private static final ImmutableBiMap<Boolean, String> LOGISTICS_SERVICE_CHARGE_STATUSES = ImmutableBiMap
            .<Boolean, String>builder()
            .put(Boolean.FALSE, "待结算")
            .put(Boolean.TRUE, "已结算")
            .build();

    private static final ImmutableBiMap<Boolean, String> WMS_SERVICE_CHARGE_STATUSES = ImmutableBiMap
            .<Boolean, String>builder()
            .put(Boolean.TRUE, "无需结算")
            .build();

    private static final ImmutableBiMap<Boolean, String> LOGISTICS_COMMISSION_CHARGE_STATUSES = ImmutableBiMap
            .<Boolean, String>builder()
            .put(Boolean.FALSE, "待扣除")
            .put(Boolean.TRUE, "已扣除")
            .build();

    private static final ImmutableBiMap<Boolean, String> WMS_COMMISSION_CHARGE_STATUSES = ImmutableBiMap
            .<Boolean, String>builder()
            .put(Boolean.FALSE, "待收取")
            .put(Boolean.TRUE, "已收取")
            .build();


    public static String getProductChargeStatus(boolean productChargeSettled) {
        return PRODUCT_CHARGE_STATUSES.get(productChargeSettled);
    }

    public static String getServiceChargeStatus(boolean serviceChargeSettled, PayChannel payChannel) {
        return PayChannel.LOGISTICS.equals(payChannel)
                ? LOGISTICS_SERVICE_CHARGE_STATUSES.get(serviceChargeSettled)
                : WMS_SERVICE_CHARGE_STATUSES.get(serviceChargeSettled);
    }

    public static String getCommissionChargeStatus(boolean commissionChargeSettled, PayChannel payChannel) {
        return PayChannel.LOGISTICS.equals(payChannel)
                ? LOGISTICS_COMMISSION_CHARGE_STATUSES.get(commissionChargeSettled)
                : WMS_COMMISSION_CHARGE_STATUSES.get(commissionChargeSettled);
    }


    public static Boolean fromProductChargeStatus(String productChargeStatus) {
        return PRODUCT_CHARGE_STATUSES.inverse().get(productChargeStatus);
    }

    public static Boolean fromServiceChargeStatus(String serviceChargeStatus, PayChannel payChannel) {
        return PayChannel.LOGISTICS.equals(payChannel)
                ? LOGISTICS_SERVICE_CHARGE_STATUSES.inverse().get(serviceChargeStatus)
                : WMS_SERVICE_CHARGE_STATUSES.inverse().get(serviceChargeStatus);
    }

    public static Boolean fromCommissionChargeStatus(String commissionChargeStatus, PayChannel payChannel) {
        return PayChannel.LOGISTICS.equals(payChannel)
                ? LOGISTICS_COMMISSION_CHARGE_STATUSES.inverse().get(commissionChargeStatus)
                : WMS_COMMISSION_CHARGE_STATUSES.inverse().get(commissionChargeStatus);
    }


}
