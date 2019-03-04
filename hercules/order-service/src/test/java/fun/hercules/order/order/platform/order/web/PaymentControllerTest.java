package fun.hercules.order.order.platform.order.web;

import fun.hercules.order.order.JUnitWebAppTest;
import fun.hercules.order.order.OrderIntegrationTestBase;
import fun.hercules.order.order.business.acg.dto.AcgResponse;
import fun.hercules.order.order.business.acg.service.AcgIntegrationService;
import fun.hercules.order.order.platform.order.dto.OfflinePaymentInfo;
import fun.hercules.order.order.platform.order.model.Payment;
import fun.hercules.order.order.platform.order.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import static fun.hercules.order.order.utils.ResourceUtils.loadFixture;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Transactional
@RunWith(SpringRunner.class)
@JUnitWebAppTest
public class PaymentControllerTest extends OrderIntegrationTestBase {
    protected final String offflinePaymentContent = loadFixture("offline_payment.json");

    @Autowired
    protected MockMvc mvc;

    @MockBean
    protected PaymentService paymentService;

    @MockBean
    protected AcgIntegrationService acgIntegrationService;

    @Test
    public void shouldCreateOfflinePayment() throws Exception {

        Payment payment = new Payment();
        payment.setId("47403444415059");

        doNothing().when(paymentService).validateOfflineOrder(any(OfflinePaymentInfo.class));
        given(paymentService.createPayment(any(OfflinePaymentInfo.class))).willReturn(payment);
        given(acgIntegrationService.updateOrderToPaid(any(OfflinePaymentInfo.class))).willReturn(new AcgResponse());

        mvc.perform(post("/order-payment/offline")
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .content(offflinePaymentContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

    }
}
