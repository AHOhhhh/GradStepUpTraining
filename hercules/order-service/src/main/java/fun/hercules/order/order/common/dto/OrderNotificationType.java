package fun.hercules.order.order.common.dto;

public enum OrderNotificationType {
    // Order info notification types
    RefundSuccessNotification("退款成功提醒"),
    ServiceOfferingNotification("服务商报价提醒"),
    WaitForPayNotification("待支付提醒"),
    FinancingSuccessNotification("融资需求通过提醒"),
    FinancingFailureNotification("融资需求未通过提醒"),
    FinancingApplicationNotification("融资授信申请提醒"),
    FinancingPlanConfirmationNotification("融资方案确认提醒"),
    OfflinePayFailureNotification("线下支付失败提醒"),

    // Order logistic info notification types
    LogisticInfoNotification("物流信息");

    private String name;

    OrderNotificationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
