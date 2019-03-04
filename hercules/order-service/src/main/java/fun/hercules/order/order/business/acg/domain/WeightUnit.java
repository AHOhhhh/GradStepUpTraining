package fun.hercules.order.order.business.acg.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.math.BigDecimal;

public enum WeightUnit {
    G(1), KG(1000), T(1000000);

    private final int times;

    WeightUnit(int times) {
        this.times = times;
    }

    @JsonCreator
    public static WeightUnit of(String lowerCaseUnit) {
        return valueOf(lowerCaseUnit.toUpperCase());
    }

    public BigDecimal convertTo(BigDecimal value, WeightUnit unit) {
        if (unit.equals(this)) {
            return value;
        } else {
            return value.multiply(BigDecimal.valueOf(this.times)).divide(BigDecimal.valueOf(unit.times));
        }
    }

    @JsonValue
    public String toLowerCase() {
        return this.name().toLowerCase();
    }
}
