package fun.hercules.order.order.platform.order.service;

import fun.hercules.order.order.JUnitWebAppTest;
import fun.hercules.order.order.OrderIntegrationTestBase;
import fun.hercules.order.order.business.acg.dto.AcgResponse;
import fun.hercules.order.order.common.exceptions.BadRequestException;
import fun.hercules.order.order.platform.exports.BusinessOrder;
import fun.hercules.order.order.platform.order.client.PaymentClient;
import fun.hercules.order.order.platform.order.dto.OfflinePaymentInfo;
import fun.hercules.order.order.platform.order.dto.OrderPageResponse;
import fun.hercules.order.order.platform.order.dto.PaymentInfo;
import fun.hercules.order.order.platform.order.dto.PaymentNotification;
import fun.hercules.order.order.platform.order.model.OrderBill;
import fun.hercules.order.order.platform.order.model.OrderStatus;
import fun.hercules.order.order.platform.order.model.OrderType;
import fun.hercules.order.order.platform.order.model.PayChannel;
import fun.hercules.order.order.platform.order.model.PayMethod;
import fun.hercules.order.order.platform.order.model.Payment;
import fun.hercules.order.order.platform.order.model.PaymentRequest;
import fun.hercules.order.order.platform.order.model.PaymentStatus;
import fun.hercules.order.order.platform.order.repository.OrderStatusRepository;
import fun.hercules.order.order.platform.order.repository.PaymentRequestRepository;
import fun.hercules.order.order.platform.user.Role;
import fun.hercules.order.order.utils.RegionCodeConverter;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import static fun.hercules.order.order.utils.JsonUtils.loadFixture;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@Slf4j
@Transactional
@RunWith(SpringRunner.class)
@JUnitWebAppTest
public class OrderServiceTest extends OrderIntegrationTestBase {
    private final String wmsOrderContent = loadFixture("full_wms_order.json");
    private final String acgOrderContent = loadFixture("/acg/create_acg_order.json");

    @MockBean
    PaymentClient paymentClient;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @MockBean
    private OrderBillService orderBillService;

    @Autowired
    private PaymentRequestRepository paymentRequestRepository;

    @Before
    public void setUp() {
        OrderBill orderBill = OrderBill.builder().id(11L).orderId("orderId").build();
        when(orderBillService.findOrderBillByOrderId(anyString())).thenReturn(orderBill);
    }

    @Test
    public void should_update_order_status_after_offline_payment() {
        when(paymentClient.getPaymentTransactions(any())).thenReturn(new ArrayList<>());

        switchRole(Role.PlatformService);

        final String wmsOrderContent = loadFixture("full_wms_order.json");
        BusinessOrder createdOrder = orderService.create("wms", wmsOrderContent);

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .amount(new BigDecimal(1000))
                .currency(Currency.getInstance(Locale.CHINA))
                .orderId(createdOrder.getId())
                .orderType(OrderType.of("wms"))
                .paymentStatus(PaymentStatus.PayInProcess)
                .build();

        PaymentRequest createdPaymentRequest = paymentRequestRepository.save(paymentRequest);

        List<String> paymentRequestIds = new ArrayList<>();
        paymentRequestIds.add(createdPaymentRequest.getId());

        PaymentInfo paymentInfo = OfflinePaymentInfo.builder()
                .paymentId("1234567890")
                .businessType("WMS")
                .orderId(createdOrder.getId())
                .currencyCode("CNY")
                .orderAmount(new BigDecimal(1000))
                .payAmount(new BigDecimal(1000))
                .payChannel(PayChannel.LOGISTICS)
                .payMethod(PayMethod.OFFLINE)
                .payCustId(currentUser.getUserId())
                .payRequestIds(paymentRequestIds)
                .bankTransactionNumber("9999999999999999")
                .bankTransactionTime("2017-10-01")
                .bankTransactionComment("test comment")
                .depositBank("招商银行")
                .collectionAccountNumber("00000000000000")
                .collectionAccountName("test account name")
                .build();

        Payment payment = paymentService.createPayment(paymentInfo);

        assertThat(payment.getStatus(), is(PaymentStatus.PayInProcess));
        assertThat(createdOrder.getBusinessType().value(), is("wms"));

        switchRole(Role.PlatformService);
        PaymentNotification paymentNotification = PaymentNotification.builder()
                .paymentUserId(currentUser.getUserId())
                .status(PaymentStatus.Fail)
                .build();

        paymentService.updatePaymentStatus(payment.getId(), paymentNotification);

        assertThat(orderService.getByOrderId(createdOrder.getId()).getStatus().toString(),
                CoreMatchers.is(OrderStatus.Type.Audited.toString()));
    }


    @Test
    public void shouldUpdateOrderStatus() throws Exception {
        switchRole(Role.EnterpriseUser);
        BusinessOrder createdOrder = orderService.create("wms", wmsOrderContent);

        orderService.update("wms", createdOrder.getId(), "{\"status\": \"OfflinePaidAwaitingConfirm\"}");
        BusinessOrder updatedOrder = orderService.getByOrderId(createdOrder.getId());
        assertThat(updatedOrder.getStatus(), is(OrderStatus.of(OrderStatus.Type.OfflinePaidAwaitingConfirm)));
    }

    @Test
    public void shouldNotUpdateOrderStatusDirectly() throws Exception {
        switchRole(Role.PlatformService);
        BusinessOrder wmsOrder = orderService.getByOrderId(orderService.create("wms", wmsOrderContent).getId());
        wmsOrder.setStatus(OrderStatus.of(OrderStatus.Type.Paid));

        assertThrows(() -> {
            orderService.updateBusinessOrder(wmsOrder);
        }, BadRequestException.class);
    }


    @Test
    public void shouldTest() throws Exception {
        assertThat(RegionCodeConverter.country2Code("中国大陆"), is("CN"));
        assertThat(RegionCodeConverter.country2Code("香港"), is("HK"));
    }

    @Test
    public void shouldTest2() throws Exception {
        assertThat(RegionCodeConverter.code2Country("CN"), is("中国大陆"));
        assertThat(RegionCodeConverter.code2Country("HK"), is("香港"));
    }

    @Test
    public void should_return_all_to_be_confirmed_orders() {
        switchRole(Role.EnterpriseUser);
        BusinessOrder createdOrder = orderService.create("acg", acgOrderContent);

        OrderPageResponse<BusinessOrder> orders = orderService.list(createdOrder.getEnterpriseId(), "acg", "ToBeConfirmed", 0, 10);

        assertThat(orders.getContent().size(), is(1));
        assertThat(orders.getContent().get(0).getStatus(), is(OrderStatus.of(OrderStatus.Type.Submitted)));
    }

    @Test
    public void should_return_all_in_progress_orders() {
        switchRole(Role.EnterpriseUser);
        BusinessOrder createdOrder = orderService.create("acg", acgOrderContent);
        createdOrder.setStatus(OrderStatus.of(OrderStatus.Type.OrderTracking));

        OrderPageResponse<BusinessOrder> orders = orderService.list(createdOrder.getEnterpriseId(), "acg", "InProgress", 0, 10);

        assertThat(orders.getContent().size(), is(1));
        assertThat(orders.getContent().get(0).getStatus(), is(OrderStatus.of(OrderStatus.Type.OrderTracking)));

    }

    @Test
    public void should_return_all_completed_orders() {
        switchRole(Role.EnterpriseUser);
        BusinessOrder createdOrder = orderService.create("acg", acgOrderContent);
        createdOrder.setStatus(OrderStatus.of(OrderStatus.Type.Closed));

        OrderPageResponse<BusinessOrder> orders = orderService.list(createdOrder.getEnterpriseId(), "acg", "Completed", 0, 10);

        assertThat(orders.getContent().size(), is(1));
        assertThat(orders.getContent().get(0).getStatus(), is(OrderStatus.of(OrderStatus.Type.Closed)));
    }

    @Test
    public void should_return_all_cancelled_orders() {
        switchRole(Role.EnterpriseUser);
        BusinessOrder createdOrder = orderService.create("acg", acgOrderContent);
        createdOrder.setStatus(OrderStatus.of(OrderStatus.Type.Cancelled));

        OrderPageResponse<BusinessOrder> orders = orderService.list(createdOrder.getEnterpriseId(), "acg", "Cancelled", 0, 10);

        assertThat(orders.getContent().size(), is(1));
        assertThat(orders.getContent().get(0).getStatus(), is(OrderStatus.of(OrderStatus.Type.Cancelled)));
    }

    private PaymentRequest createPaymentRequest(BusinessOrder order) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setOrderId(order.getId());
        paymentRequest.setAmount(BigDecimal.valueOf(123.5));
        paymentRequest.setPaidTime(Instant.now());
        paymentRequest.setPaymentStatus(PaymentStatus.PayInProcess);
        paymentRequest.setCurrency(Currency.getInstance("CNY"));

        return paymentRequestRepository.save(paymentRequest);
    }

}