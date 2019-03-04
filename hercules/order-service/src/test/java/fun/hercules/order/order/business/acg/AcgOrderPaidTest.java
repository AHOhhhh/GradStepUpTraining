package fun.hercules.order.order.business.acg;

import fun.hercules.order.order.JUnitWebAppTest;
import fun.hercules.order.order.business.acg.domain.AcgOrder;
import fun.hercules.order.order.platform.exports.BusinessOrder;
import fun.hercules.order.order.platform.order.client.PaymentClient;
import fun.hercules.order.order.platform.order.dto.PaymentNotification;
import fun.hercules.order.order.platform.order.model.OrderStatus;
import fun.hercules.order.order.platform.order.model.OrderType;
import fun.hercules.order.order.platform.order.model.Payment;
import fun.hercules.order.order.platform.order.model.PaymentRequest;
import fun.hercules.order.order.platform.order.model.PaymentStatus;
import fun.hercules.order.order.platform.order.repository.PaymentRepository;
import fun.hercules.order.order.platform.order.repository.PaymentRequestRepository;
import fun.hercules.order.order.platform.user.Role;
import fun.hercules.order.order.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

import static fun.hercules.order.order.utils.JsonUtils.loadFixture;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Transactional
@RunWith(SpringRunner.class)
@JUnitWebAppTest
public class AcgOrderPaidTest extends AcgOrderTestBase {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentRequestRepository paymentRequestRepository;

    @MockBean
    private PaymentClient paymentClient;

    @Test
    public void shouldUpdateStatusToPaid() throws Exception {
        BusinessOrder order = orderService.create(OrderType.of(OrderType.Type.ACG).getName(), orderContent);
        order.setStatus(OrderStatus.of(OrderStatus.Type.WaitForPay));
        order = acgOrderRepository.save((AcgOrder) order);
        createPaymentRequest(order);
        switchRole(Role.PlatformService);
        Payment payment = createPayment(order);
        PaymentNotification notification = createPaymentNotification(payment);
        when(currentUser.getRole()).thenReturn(Role.PlatformService.toString());
        mvc.perform(post("/order-payment/{paymentId}", payment.getId())
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .content(JsonUtils.marshal(notification))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        AcgOrder acgOrder = acgOrderRepository.getOne(order.getId());
        assertThat(acgOrder.getStatus(), is(OrderStatus.of(OrderStatus.Type.OrderTracking)));
    }

    private Payment createPayment(BusinessOrder order) throws java.io.IOException {
        Payment payment = JsonUtils.getMapper().readValue(loadFixture("acg/payment.json"), Payment.class);
        payment.setPaymentRequests(paymentRequestRepository.findByOrderId(order.getId()));
        return paymentRepository.save(payment);
    }

    private PaymentNotification createPaymentNotification(Payment payment) {
        PaymentNotification notification = new PaymentNotification();
        notification.setPaymentId(payment.getId());
        notification.setStatus(PaymentStatus.Success);
        notification.setPaidTime(Instant.now());

        return notification;
    }

    private PaymentRequest createPaymentRequest(BusinessOrder order) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setOrderId(order.getId());
        paymentRequest.setOrderType(OrderType.of(OrderType.Type.ACG));
        paymentRequest.setAmount(BigDecimal.valueOf(123.5));
        paymentRequest.setPaidTime(Instant.now());
        paymentRequest.setPaymentStatus(PaymentStatus.PayInProcess);
        paymentRequest.setCurrency(Currency.getInstance("CNY"));

        return paymentRequestRepository.save(paymentRequest);
    }
}
