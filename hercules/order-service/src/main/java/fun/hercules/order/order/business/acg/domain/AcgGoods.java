package fun.hercules.order.order.business.acg.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fun.hercules.order.order.platform.exports.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Where(clause = EntityBase.SKIP_DELETED_CLAUSE)
@SQLDelete(sql = "UPDATE acg_goods SET deleted = true WHERE id = ? AND version = ?")
@ToString(callSuper = true, exclude = "order")
public class AcgGoods extends EntityBase implements GoodsTrait {
    @Id
    @JsonIgnore
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JsonIgnore
    private AcgOrder order;

    @NotNull
    @javax.validation.constraints.Size(min = 1, max = 50)
    private String name;

    @Valid
    @NotNull
    private Size size;

    @Valid
    @NotNull
    private Weight weight;

    @DecimalMin("0.01")
    @DecimalMax("99999999.99")
    private BigDecimal price;

    @NotNull
    @Min(1)
    @Max(99999999)
    private Integer quantity;

    @Min(1)
    @NotNull
    private Integer packageQuantity;

    @javax.validation.constraints.Size(max = 100)
    private String declarationInfo;

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
        BigDecimal height = size.getUnit().convertTo(size.getHeight(), LengthUnit.CM);
        BigDecimal width = size.getUnit().convertTo(size.getWidth(), LengthUnit.CM);
        BigDecimal length = size.getUnit().convertTo(size.getLength(), LengthUnit.CM);
        return length.intValue() + "*" + width.intValue() + "*" + height.intValue() + "/" + packageQuantity;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Size {
        @DecimalMin("0.01")
        @DecimalMax("99999999.99")
        private BigDecimal length;

        @DecimalMin("0.01")
        @DecimalMax("99999999.99")
        private BigDecimal width;

        @DecimalMin("0.01")
        @DecimalMax("99999999.99")
        private BigDecimal height;

        @Column(name = "size_unit", columnDefinition = "VARCHAR(10)")
        @Enumerated(EnumType.STRING)
        @NotNull
        private LengthUnit unit;

        @JsonIgnore
        @Transient
        public BigDecimal getVolume(LengthUnit unit) {
            return this.unit.convertTo(length, unit)
                    .multiply(this.unit.convertTo(width, unit))
                    .multiply(this.unit.convertTo(height, unit));
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Weight {
        @Column(name = "weight")
        @DecimalMin("0.01")
        @DecimalMax("99999999.99")
        private BigDecimal value;

        @Column(name = "weight_unit", columnDefinition = "VARCHAR(10)")
        @Enumerated(EnumType.STRING)
        private WeightUnit unit;

        @JsonIgnore
        @Transient
        public BigDecimal getWeight(WeightUnit unit) {
            return unit.convertTo(value, unit);
        }

    }
}
