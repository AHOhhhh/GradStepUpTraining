package fun.hercules.order.order.business.wms.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import fun.hercules.order.order.common.converter.GenericJsonValueConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Converter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargingRule implements Serializable {
    @JsonProperty(required = true)
    private Integer quantityFrom;

    private Integer quantityTo;

    @JsonProperty(required = true)
    private BigDecimal unitPrice;

    @Converter
    public static class ChargingRulesConverter extends GenericJsonValueConverter<List<ChargingRule>> {
        public ChargingRulesConverter() {
            super(new TypeReference<List<ChargingRule>>() {});
        }
    }
}
