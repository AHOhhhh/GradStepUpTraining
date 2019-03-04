package fun.hercules.order.order.platform.order.service;

import fun.hercules.order.order.business.wms.domain.WmsOrder;
import fun.hercules.order.order.platform.order.model.OrderStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class OrderFieldsMapperTest {
    private OrderFieldsMapper fieldsMapper;

    @Before
    public void before() {
        fieldsMapper = new OrderFieldsMapper(WmsOrder.class);
    }


    @Test
    public void map() throws NoSuchFieldException {
        WmsOrder oldOrder = new WmsOrder();
        oldOrder.setServiceIntro("serviceIntro");
        oldOrder.setSubscriptionId("1234");
        oldOrder.setStatus(OrderStatus.of(OrderStatus.Type.Submitted));
        WmsOrder newOrder = new WmsOrder();
        fieldsMapper.map(oldOrder, newOrder);
        assertEquals("serviceIntro", newOrder.getServiceIntro());
        System.out.println(newOrder.getStatus());
        Assert.assertEquals(OrderStatus.Type.Submitted.name(), newOrder.getStatus().getName());
        assertNull(newOrder.getSubscriptionId());
    }
}