package fun.hercules.order.order.business.acg;

import com.jayway.jsonpath.JsonPath;
import fun.hercules.order.order.JUnitWebAppTest;
import fun.hercules.order.order.business.acg.domain.AcgOrder;
import fun.hercules.order.order.business.acg.dto.AcgResponse;
import fun.hercules.order.order.business.acg.dto.order.AcgCreateOrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Transactional
@RunWith(SpringRunner.class)
@JUnitWebAppTest
public class AcgOrderControllerTest extends AcgOrderTestBase {

    @Test
    public void shouldCreateAcgOrder() throws Exception {
        AcgCreateOrderResponse createOrderResponse = new AcgCreateOrderResponse();
        createOrderResponse.setStatus(AcgResponse.Status.SUCCESS);

        given(acgIntegrationService.createOrder(any(AcgOrder.class))).willReturn(createOrderResponse);

        MvcResult result = mvc.perform(post("/acg/orders")
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .content(orderContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();

        String orderId = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id").toString();
        Optional<AcgOrder> order = acgOrderRepository.findOneById(orderId);
        assertTrue(order.isPresent());
        assertThat(order.get().getOrderSubTypes(), hasItems("PickUp", "AirCargo", "DropOff"));
    }
}
