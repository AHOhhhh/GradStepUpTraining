package fun.hercules.order.order.business.wms;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import fun.hercules.order.order.JUnitWebAppTest;
import fun.hercules.order.order.OrderIntegrationTestBase;
import fun.hercules.order.order.business.wms.domain.WmsOrder;
import fun.hercules.order.order.business.wms.domain.WmsOrderType;
import fun.hercules.order.order.platform.exports.BusinessOrder;
import fun.hercules.order.order.platform.order.model.OrderStatus;
import fun.hercules.order.order.platform.order.service.OrderService;
import fun.hercules.order.order.platform.user.Privilege;
import fun.hercules.order.order.platform.user.Role;
import fun.hercules.order.order.utils.JsonUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static fun.hercules.order.order.utils.ResourceUtils.loadFixture;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@RunWith(SpringRunner.class)
@JUnitWebAppTest
public class WmsOrderControllerSearchTest extends OrderIntegrationTestBase {
    private final String wmsOrderContent = loadFixture("full_wms_order.json");

    @Autowired
    private MockMvc mvc;

    @Autowired
    private OrderService orderService;

    private List<String> userList = ImmutableList.of("user1", "user2");

    private List<String> enterpriseList = ImmutableList.of("enterpriseA", "enterpriseB");

    private List<OrderStatus.Type> orderStatusList = ImmutableList.of(
            OrderStatus.Type.Submitted, OrderStatus.Type.Audited,
            OrderStatus.Type.Paid, OrderStatus.Type.Closed);

    private List<WmsOrderType.Type> wmsOrderTypeList = ImmutableList.of(
            WmsOrderType.Type.Renew, WmsOrderType.Type.Recharge);


    private List<BusinessOrder> orderFixtures;

    private Configuration jsonPathConfiguration = Configuration.builder()
            .mappingProvider(new JacksonMappingProvider(JsonUtils.getMapper()))
            .jsonProvider(new JacksonJsonProvider(JsonUtils.getMapper()))
            .build();

    @Before
    public void createFixtures() throws Exception {
        when(currentUser.getRole()).thenReturn(Role.PlatformAdmin.toString());
        when(currentUser.getPrivileges()).thenReturn(ImmutableSet.of(Privilege.of(Privilege.Type.WmsOrderAccessPrivilege)));
        orderFixtures = new ArrayList<>();
        DocumentContext wmsOrderJson = JsonPath.parse(wmsOrderContent);
        for (String enterpriseId : enterpriseList) {
            // Wms New Order should only create once
            orderFixtures.add(createWmsOrder(wmsOrderJson, userList.get(0), enterpriseId,
                    WmsOrderType.Type.Open, orderStatusList.get(0)));
            for (String userId : userList) {
                for (WmsOrderType.Type wmsType : wmsOrderTypeList) {
                    for (OrderStatus.Type status : orderStatusList) {
                        orderFixtures.add(createWmsOrder(wmsOrderJson, userId, enterpriseId, wmsType, status));
                    }
                }
            }
        }
    }

    private BusinessOrder createWmsOrder(DocumentContext wmsOrderJson, String userId,
                                         String enterpriseId, WmsOrderType.Type wmsType, OrderStatus.Type status) {
        when(currentUser.getUserId()).thenReturn(userId);
        when(currentUser.getEnterpriseId()).thenReturn(enterpriseId);
        wmsOrderJson.set("$.type", wmsType.name());
        wmsOrderJson.set("$.status", status.name());

        return orderService.create("wms", wmsOrderJson.jsonString());
    }

    @Test
    public void shouldReturnAllOrdersWithoutFilter() throws Exception {
        List<BusinessOrder> orders = getOrdersFromPagedResult(mvc.perform(get("/wms/orders")
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .param("size", "100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());
        Assert.assertThat(orders.size(), is(orderFixtures.size()));
    }

    private List<BusinessOrder> getOrdersFromPagedResult(String result) throws Exception {
        return JsonPath.parse(result, jsonPathConfiguration).read("$.content", new TypeRef<List<WmsOrder>>() {
        }).stream().collect(Collectors.toList());
    }

}
