package fun.hercules.order.order.platform.order.model;


public enum PayMethod {
    ONLINE("线上"), OFFLINE("线下"), DEFERMENT("后付");

    private String value;

    PayMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
