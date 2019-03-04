package fun.hercules.order.order.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BusinessType {
    WMS("warehouse management system"),
    ACG("air cargo");

    private final String description;

    BusinessType(String description) {
        this.description = description;
    }

    @JsonCreator
    public static BusinessType of(String value) {
        return valueOf(value.toUpperCase());
    }

    public String getDescription() {
        return description;
    }

    @JsonValue
    public String getValue() {
        return name().toLowerCase();
    }
}
