package fun.hercules.order.order.platform.order.service;

import fun.hercules.order.order.platform.order.model.OrderBill;
import fun.hercules.order.order.platform.order.repository.OrderBillRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderBillServiceTest {

    private OrderBillService service;

    @Before
    public void setUp() throws Exception {
        OrderBill orderBill1 = OrderBill.builder().orderId("orderId1").build();
        orderBill1.setCreatedAt(Instant.ofEpochSecond(1514949920));
        OrderBill orderBill2 = OrderBill.builder().orderId("orderId2").build();
        orderBill2.setCreatedAt(Instant.ofEpochSecond(1515036320));

        OrderBillRepository repository = mock(OrderBillRepository.class);
        when(repository.findByOrderId(anyString())).thenReturn(Optional.of(orderBill1));
        when(repository.findByOrderTypeInAndCreatedAtBetween(Arrays.asList("acg"), Instant.parse("2018-01-01T16:00:00Z"),
                Instant.parse("2018-01-03T15:59:59.999999999Z"), new PageRequest(0, 10))).thenReturn(new PageImpl(Arrays.asList(orderBill1, orderBill2)));
        when(repository.findByOrderTypeIn(Arrays.asList("acg"), new PageRequest(0, 10))).thenReturn(new PageImpl(Arrays.asList(orderBill2)));

        service = new OrderBillService(repository, null);
    }

    @Test
    public void shouldFindPageOfOrderBill() throws Exception {
        Page<OrderBill> orderBills = service.findPageableOrderBills(LocalDate.of(2018, 1, 2),
                LocalDate.of(2018, 1, 3), Arrays.asList("acg"), new PageRequest(0, 10));
        assertEquals(2, orderBills.getNumberOfElements());
    }

    @Test
    public void shouldNotFindPageOfOrderBillWithWrongOrderType() throws Exception {
        Page<OrderBill> orderBills = service.findPageableOrderBills(LocalDate.of(2018, 1, 2),
                LocalDate.of(2018, 1, 3), Arrays.asList("wms"), new PageRequest(0, 10));
        assertNull(orderBills);
    }

    @Test
    public void shouldReturnOneOrderBillWithFromOrToDateIsNull() throws Exception {
        Page<OrderBill> orderBills = service.findPageableOrderBills(null,
                LocalDate.of(2018, 1, 3), Arrays.asList("acg"), new PageRequest(0, 10));
        assertEquals(1, orderBills.getNumberOfElements());
    }
}