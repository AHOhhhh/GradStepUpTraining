package fun.hercules.order.order.platform.order.model;

// TODO: 09/01/2018 add dadaowuliu when it's needed
public enum PayChannel {
    LOGISTICS("平台"),
    WMS("服务商");

    private String value;

    PayChannel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
