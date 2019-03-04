package fun.hercules.order.order.platform.order.utils;

import fun.hercules.order.order.platform.order.dto.OrderBillDTO;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReflectUtilTest {

    @Test
    public void getFieldValueByFieldName() {
        OrderBillDTO orderBill = OrderBillDTO.builder().orderId("123456").build();
        Object orderId = ReflectUtil.getFieldValueByFieldName("orderId", orderBill);
        System.out.println(orderId);
        assertEquals("123456", orderId);
    }
}