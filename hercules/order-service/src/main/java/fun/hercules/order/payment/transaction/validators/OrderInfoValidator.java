package fun.hercules.order.payment.transaction.validators;

import fun.hercules.order.order.common.errors.ErrorCode;
import fun.hercules.order.order.common.exceptions.BadRequestException;
import fun.hercules.order.payment.transaction.request.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderInfoValidator {


    @Autowired
    public OrderInfoValidator() {

    }

    public void validate(OrderInfo orderInfo) {

        // TODO: 18/12/2017 validate for offline

        checkPayAmount(orderInfo.getPayAmount());
        checkOrderAmount(orderInfo.getOrderAmount());
    }

    private void checkOrderAmount(BigDecimal money) {
        if (money.compareTo(new BigDecimal("0.01")) < 0) {
            throw new BadRequestException(ErrorCode.MONEY_TOO_LITTLE, "orderAmount money is too little");
        }
    }

    private void checkPayAmount(BigDecimal money) {
        if (money.compareTo(new BigDecimal("0.01")) < 0) {
            throw new BadRequestException(ErrorCode.MONEY_TOO_LITTLE, "payAmount money is too little");
        }
    }
}
