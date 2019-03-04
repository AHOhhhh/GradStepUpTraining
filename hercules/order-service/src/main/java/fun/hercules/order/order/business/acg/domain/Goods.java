package fun.hercules.order.order.business.acg.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods implements GoodsTrait {

    private Weight weight;

    private Size size;

    private Integer quantity;

    private Integer packageQuantity;

    @Override
    public BigDecimal getWeight(WeightUnit unit) {
        return weight.getWeight(unit);
    }

    @Override
    public BigDecimal getVolume(LengthUnit unit) {
        return size.getVolume(unit);
    }

    @Override
    public String getGoodsSize() {
        LengthUnit lengthUnit = LengthUnit.of(this.size.getUnit());
        BigDecimal height = lengthUnit.convertTo(BigDecimal.valueOf(size.getHeight()), LengthUnit.CM);
        BigDecimal width = lengthUnit.convertTo(BigDecimal.valueOf(size.getWidth()), LengthUnit.CM);
        BigDecimal length = lengthUnit.convertTo(BigDecimal.valueOf(size.getLength()), LengthUnit.CM);
        return length.intValue() + "*" + width.intValue() + "*" + height.intValue() + "/" + packageQuantity;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Weight {

        private BigDecimal value;

        private String unit;

        @JsonIgnore
        @Transient
        public BigDecimal getWeight(WeightUnit unit) {
            return unit.convertTo(value, unit);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Size {

        private Double length;

        private Double width;

        private Double height;

        private String unit;

        @JsonIgnore
        @Transient
        public BigDecimal getVolume(LengthUnit unit) {
            LengthUnit lengthUnit = LengthUnit.of(this.unit);
            return lengthUnit.convertTo(BigDecimal.valueOf(length), unit)
                    .multiply(lengthUnit.convertTo(BigDecimal.valueOf(width), unit))
                    .multiply(lengthUnit.convertTo(BigDecimal.valueOf(height), unit));
        }
    }
}



