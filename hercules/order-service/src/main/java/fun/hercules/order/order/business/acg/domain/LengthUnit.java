package fun.hercules.order.order.business.acg.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.math.BigDecimal;

public enum LengthUnit {
    MM(1), CM(10), DM(100), M(1000);

    private final int times;

    LengthUnit(int times) {
        this.times = times;
    }

    @JsonCreator
    public static LengthUnit of(String lowerCaseUnit) {
        return valueOf(lowerCaseUnit.toUpperCase());
    }

    public BigDecimal convertTo(BigDecimal value, LengthUnit unit) {
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
