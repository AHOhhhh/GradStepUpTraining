package fun.hercules.order.order.business.acg.domain;

import java.math.BigDecimal;

public interface GoodsTrait {

    BigDecimal getWeight(WeightUnit unit);

    BigDecimal getVolume(LengthUnit unit);

    String getGoodsSize();

    Integer getQuantity();

    Integer getPackageQuantity();
}



