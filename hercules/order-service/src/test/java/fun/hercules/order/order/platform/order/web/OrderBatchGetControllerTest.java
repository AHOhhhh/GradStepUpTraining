package fun.hercules.order.order.platform.order.web;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import fun.hercules.order.order.JUnitWebAppTest;
import fun.hercules.order.order.OrderIntegrationTestBase;
import fun.hercules.order.order.business.acg.client.AcgBusinessClient;
import fun.hercules.order.order.platform.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static fun.hercules.order.order.utils.ResourceUtils.loadFixture;
import static org.hamcrest.collection.IsIn.isIn;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Transactional
@RunWith(SpringRunner.class)
@JUnitWebAppTest
public class OrderBatchGetControllerTest extends OrderIntegrationTestBase {
    private final String orderContent = loadFixture("acg/create_acg_order.json");

    @Autowired
    private MockMvc mvc;

    @Autowired
    private OrderService orderService;

    @MockBean
    private AcgBusinessClient acgBusinessClient;

    private Set<String> orderIds;

    @Before
    public void setUpAcgBusinessClient() {
        orderIds = IntStream.range(0, 5)
                .mapToObj(operand -> orderService.create("acg", orderContent).getId())
                .collect(Collectors.toSet());
    }


    @Test
    public void shouldListAcgOrdersById() throws Exception {
        DocumentContext response = JsonPath.parse(mvc.perform(get("/acg/orders")
                .param("ids", orderIds.toArray(new String[0]))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(orderIds.size()))
                .andReturn().getResponse().getContentAsString());

        IntStream.range(0, orderIds.size()).forEach(index ->
                assertThat(response.read(String.format("$[%s].id", index)), isIn(orderIds)));
    }

    @Test
    public void shouldVerifyAcgOrdersIds() throws Exception {
        orderIds.add("");
        orderIds.add(null);
        mvc.perform(get("/acg/orders")
                .param("ids", orderIds.toArray(new String[0]))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFillNullForNotExistedIds() throws Exception {
        ArrayList<String> requestIds = new ArrayList<>(orderIds);
        requestIds.add("not-exist");
        DocumentContext response = JsonPath.parse(mvc.perform(get("/acg/orders")
                .param("ids", requestIds.toArray(new String[0]))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(orderIds.size()))
                .andReturn().getResponse().getContentAsString());

        IntStream.range(0, orderIds.size()).forEach(index ->
                assertThat(response.read(String.format("$[%s].id", index)), isIn(orderIds)));
    }
}
