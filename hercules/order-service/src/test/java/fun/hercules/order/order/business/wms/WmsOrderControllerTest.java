package fun.hercules.order.order.business.wms;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import fun.hercules.order.order.JUnitWebAppTest;
import fun.hercules.order.order.OrderIntegrationTestBase;
import fun.hercules.order.order.business.wms.domain.ChargingRule;
import fun.hercules.order.order.business.wms.domain.WmsOrder;
import fun.hercules.order.order.business.wms.domain.WmsOrderType;
import fun.hercules.order.order.business.wms.repository.WmsOrderRepository;
import fun.hercules.order.order.clients.UserServiceClient;
import fun.hercules.order.order.common.constants.ConfigProperties;
import fun.hercules.order.order.common.dto.Contact;
import fun.hercules.order.order.common.dto.OrderNotification;
import fun.hercules.order.order.common.errors.ErrorCode;
import fun.hercules.order.order.platform.order.dto.Cancellation;
import fun.hercules.order.order.platform.order.dto.PaymentNotification;
import fun.hercules.order.order.platform.order.model.CancelReason;
import fun.hercules.order.order.platform.order.model.OrderStatus;
import fun.hercules.order.order.platform.order.model.OrderType;
import fun.hercules.order.order.platform.order.model.PayChannel;
import fun.hercules.order.order.platform.order.model.PayMethod;
import fun.hercules.order.order.platform.order.model.Payment;
import fun.hercules.order.order.platform.order.model.PaymentRequest;
import fun.hercules.order.order.platform.order.model.PaymentStatus;
import fun.hercules.order.order.platform.order.repository.PaymentRepository;
import fun.hercules.order.order.platform.order.repository.PaymentRequestRepository;
import fun.hercules.order.order.platform.user.Privilege;
import fun.hercules.order.order.platform.user.Role;
import fun.hercules.order.order.utils.DateUtils;
import fun.hercules.order.order.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Currency;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static fun.hercules.order.order.utils.JsonUtils.loadFixture;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Transactional
@RunWith(SpringRunner.class)
@JUnitWebAppTest
public class WmsOrderControllerTest extends OrderIntegrationTestBase {
    private final String orderContent = loadFixture("create_wms_order.json");

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WmsOrderRepository wmsOrderRepository;

    @Autowired
    private PaymentRequestRepository paymentRequestRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Test
    public void shouldCreateWmsOrder() throws Exception {
        MvcResult result = mvc.perform(post("/wms/orders")
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .content(orderContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();

        String orderId = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id").toString();
        Optional<WmsOrder> order = Optional.of(wmsOrderRepository.getOne(orderId));
        assertTrue(order.isPresent());
        assertThat(order.get().getId(), is(orderId));
    }

    @Test
    @Ignore //for temp
    public void shouldAuditWmsOrderAndUpdateStatus() throws Exception {
        when(currentUser.getRole()).thenReturn(Role.PlatformAdmin.toString());
        when(currentUser.getPrivileges()).thenReturn(new HashSet<Privilege>(){ {
            add(Privilege.of(Privilege.Type.WmsOrderAccessPrivilege));
        } });
        WmsOrder order = generateWmsOrder();
        String tomorrow = LocalDate.now(ConfigProperties.getInstance().getTimeZone()).plusDays(1).toString();
        String nextYear = LocalDate.now(ConfigProperties.getInstance().getTimeZone()).plusYears(1).toString();
        DocumentContext content = JsonPath.parse(loadFixture("updated_wms_order_to_audited.json"))
                .set("$.effectiveFrom", tomorrow)
                .set("$.effectiveTo", nextYear);
        mvc.perform(post("/wms/orders/" + order.getId())
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .content(content.jsonString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        order = wmsOrderRepository.getOne(order.getId());

        assertThat(DateUtils.format("yyyy-MM-dd", order.getEffectiveFrom()), is(tomorrow));
        assertThat(DateUtils.format("yyyy-MM-dd", order.getEffectiveTo()), is(nextYear));
        assertThat(order.getApprovedPrice(), closeTo(BigDecimal.valueOf(1234), BigDecimal.valueOf(0.1)));
        assertThat(order.getChargingRules().size(), is(2));
        assertThat(order.getCurrency(), is(Currency.getInstance("CNY")));
        ChargingRule firstChargingRule = order.getChargingRules().get(0);
        assertThat(firstChargingRule.getQuantityFrom(), is(0));
        assertThat(firstChargingRule.getQuantityTo(), is(100));
        assertThat(firstChargingRule.getUnitPrice(), closeTo(BigDecimal.valueOf(0.28), BigDecimal.valueOf(0.1)));
        ChargingRule secondChargingRule = order.getChargingRules().get(1);
        assertThat(secondChargingRule.getQuantityFrom(), is(100));
        assertThat(secondChargingRule.getQuantityTo(), is(200));
        assertThat(secondChargingRule.getUnitPrice(), closeTo(BigDecimal.valueOf(0.15), BigDecimal.valueOf(0.1)));
        MatcherAssert.assertThat(order.getStatus(), CoreMatchers.is(new OrderStatus(OrderStatus.Type.Audited)));

        verify(userServiceClient, times(1)).createOrderNotification(any(OrderNotification.class));
    }

    @Test
    public void shouldOnlyCreateOneWmsOrderForOneUser() throws Exception {
        mvc.perform(post("/wms/orders")
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .content(orderContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
        mvc.perform(post("/wms/orders")
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .content(orderContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldAbleToCreateWmsOrderIfPreviousWmsOrderIsCancelled() throws Exception {
        String orderId = JsonPath.parse(mvc.perform(post("/wms/orders")
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .content(orderContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn().getResponse().getContentAsString())
                .read("$.id", String.class);

        mvc.perform(post("/wms/orders/{id}/cancellation", orderId)
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .content(JsonUtils.marshal(new Cancellation(CancelReason.of(CancelReason.Type.ManualCancellation))))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(post("/wms/orders")
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .content(orderContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldGetOrderById() throws Exception {
        WmsOrder wmsOrder = generateWmsOrder();

        String orderId = wmsOrder.getId();
        when(currentUser.getRole()).thenReturn(Role.PlatformService.toString());
        mvc.perform(get("/wms/orders/{id}", orderId)
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contact.address", is("陕西省西安市雁塔区")))
                .andExpect(jsonPath("$.contact.name", is("王月")))
                .andExpect(jsonPath("$.contact.cellphone", is("13129908653")))
                .andExpect(jsonPath("$.contact.telephone", is("+029 87654321")))
                .andExpect(jsonPath("$.userId", is("123")))
                .andExpect(jsonPath("$.enterpriseId", is("001")));
    }

    private WmsOrder generateWmsOrder() {
        return generateWmsOrder(wmsOrder -> {
        });
    }

    private WmsOrder generateWmsOrder(Consumer<WmsOrder> orderModifier) {
        WmsOrder wmsOrder = new WmsOrder();
        wmsOrder.setCreatedAt(Instant.EPOCH);
        wmsOrder.setStatus(new OrderStatus(OrderStatus.Type.Submitted));
        wmsOrder.setType(WmsOrderType.of(WmsOrderType.Type.Open));
        Contact contact = new Contact();
        contact.setAddress("陕西省西安市雁塔区");
        contact.setName("王月");
        contact.setCellphone("13129908653");
        contact.setTelephone("+029 87654321");
        wmsOrder.setContact(contact);
        wmsOrder.setId(UUID.randomUUID().toString());
        wmsOrder.setSubscriptionId(UUID.randomUUID().toString());
        wmsOrder.setUserId("123");
        wmsOrder.setMinPrice(100);
        wmsOrder.setUserRole("EnterpriseAdmin");
        wmsOrder.setEnterpriseId("001");
        wmsOrder.setVendor("wms");
        orderModifier.accept(wmsOrder);
        wmsOrder = wmsOrderRepository.save(wmsOrder);
        return wmsOrder;
    }

    @Test
    public void shouldStoreChangesAfterOrderStatusChanged() throws Exception {
        when(currentUser.getRole()).thenReturn(Role.PlatformAdmin.toString());
        when(currentUser.getPrivileges()).thenReturn(new HashSet<Privilege>(){ {
            add(Privilege.of(Privilege.Type.WmsOrderAccessPrivilege));
        } });
        WmsOrder order = generateWmsOrder();
        String orderId = order.getId();
        DocumentContext content = JsonPath.parse(loadFixture("update_wms_order.json"))
                .set("$.effectiveFrom", LocalDate.now(ConfigProperties.getInstance().getTimeZone()).toString())
                .set("$.effectiveTo", LocalDate.now(ConfigProperties.getInstance().getTimeZone()).plusYears(1).toString());
        mvc.perform(post("/wms/orders/{id}", orderId)
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .content(content.jsonString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdateWmsOrder() throws Exception {
        when(currentUser.getRole()).thenReturn(Role.PlatformAdmin.toString());
        when(currentUser.getPrivileges()).thenReturn(new HashSet<Privilege>(){ {
            add(Privilege.of(Privilege.Type.WmsOrderAccessPrivilege));
        } });
        WmsOrder order = generateWmsOrder();
        DocumentContext content = JsonPath.parse(loadFixture("update_wms_order.json"))
                .set("$.effectiveFrom", LocalDate.now(ConfigProperties.getInstance().getTimeZone()).toString())
                .set("$.effectiveTo", LocalDate.now(ConfigProperties.getInstance().getTimeZone()).plusYears(1).toString());
        mvc.perform(post("/wms/orders/" + order.getId())
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .content(content.jsonString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        order = wmsOrderRepository.getOne(order.getId());

        assertThat(order.getEffectiveFrom(), is(LocalDate.now(ConfigProperties.getInstance().getTimeZone())));
        assertThat(order.getEffectiveTo(), is(LocalDate.now(ConfigProperties.getInstance().getTimeZone()).plusYears(1)));
        assertThat(order.getApprovedPrice(), closeTo(BigDecimal.valueOf(1234), BigDecimal.valueOf(0.1)));
        assertThat(order.getServiceIntro(), is("service introduction"));
        assertThat(order.getChargingRules().size(), is(2));
        assertThat(order.getMaxPrice(), is(100));
        assertThat(order.getMinPrice(), is(10));
        ChargingRule firstChargingRule = order.getChargingRules().get(0);
        assertThat(firstChargingRule.getQuantityFrom(), is(0));
        assertThat(firstChargingRule.getQuantityTo(), is(100));
        assertThat(firstChargingRule.getUnitPrice(), closeTo(BigDecimal.valueOf(0.28), BigDecimal.valueOf(0.1)));
        ChargingRule secondChargingRule = order.getChargingRules().get(1);
        assertThat(secondChargingRule.getQuantityFrom(), is(100));
        assertNull(secondChargingRule.getQuantityTo());
        assertThat(secondChargingRule.getUnitPrice(), closeTo(BigDecimal.valueOf(0.15), BigDecimal.valueOf(0.1)));
    }

    @Test
    public void shouldThrowJsonParseExceptionWhenUpdateWmsOrderWithWrongType() throws Exception {
        WmsOrder order = generateWmsOrder();

        DocumentContext content = JsonPath.parse(loadFixture("update_wms_order.json"))
                .set("$.maxPrice", "ssss");

        mvc.perform(post("/wms/orders/" + order.getId())
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .content(content.jsonString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_REQUEST.getCode()));
    }

    @Test
    public void shouldReturnDeniedWhenUserGetOtherEnterpriseOrder() throws Exception {
        WmsOrder order = generateWmsOrder();
        mvc.perform(get("/wms/orders/{id}", order.getId())
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }


    @Test
    public void shouldDeniedWhenUserDeleteOrder() throws Exception {
        when(currentUser.getRole()).thenReturn("EnterpriseUser");
        WmsOrder order = generateWmsOrder();

        mvc.perform(post("/wms/orders/{id}/deletion", order.getId())
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private PaymentNotification createPaymentNotification(Payment payment) {
        PaymentNotification notification = new PaymentNotification();
        notification.setPaymentId(payment.getId());
        notification.setStatus(PaymentStatus.Success);
        return notification;
    }

    private Payment createPayment(String orderId) {
        Payment payment = new Payment();
        payment.setCurrency(Currency.getInstance("CNY"));
        payment.setStatus(PaymentStatus.PayInProcess);
        payment.setPayRepeated(false);
        payment.setAmount(BigDecimal.valueOf(100.2));
        payment.setPaidTime(Instant.now());
        payment.setPayChannel(PayChannel.LOGISTICS.name());
        payment.setPayMethod(PayMethod.ONLINE.name());
        payment.setPaymentRequests(paymentRequestRepository.findByOrderId(orderId));
        payment = paymentRepository.save(payment);
        return payment;
    }

    private PaymentRequest createPaymentRequest(WmsOrder order) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setCurrency(Currency.getInstance("CNY"));
        paymentRequest.setAmount(BigDecimal.valueOf(100.2));
        paymentRequest.setOrderType(OrderType.of(OrderType.Type.WMS));
        paymentRequest.setOrderId(order.getId());
        paymentRequest.setPaidTime(Instant.now());
        paymentRequest.setPaymentStatus(PaymentStatus.PayInProcess);
        return paymentRequestRepository.save(paymentRequest);
    }
}
