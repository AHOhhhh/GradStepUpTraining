package fun.hercules.order.order.platform.exports;

import java.math.BigDecimal;
import java.util.Currency;

public interface Payable {
    BigDecimal getTotalPrice();

    Currency getCurrency();
}
