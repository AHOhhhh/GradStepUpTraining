package fun.hercules.order.order.business.wms;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import fun.hercules.order.order.JUnitWebAppTest;
import fun.hercules.order.order.OrderIntegrationTestBase;
import fun.hercules.order.order.business.wms.domain.WmsOrder;
import fun.hercules.order.order.business.wms.domain.WmsOrderType;
import fun.hercules.order.order.business.wms.repository.WmsOrderRepository;
import fun.hercules.order.order.common.constants.ConfigProperties;
import fun.hercules.order.order.common.dto.Contact;
import fun.hercules.order.order.platform.order.model.OrderStatus;
import fun.hercules.order.order.platform.user.Privilege;
import fun.hercules.order.order.platform.user.Role;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static fun.hercules.order.order.utils.JsonUtils.loadFixture;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@RunWith(SpringRunner.class)
@JUnitWebAppTest
public class FunctionalTest extends OrderIntegrationTestBase {
    private final String orderCreateContent = loadFixture("create_wms_order.json");

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WmsOrderRepository wmsOrderRepository;


    @Test
    public void shouldProcessWmsNewOrder() throws Exception {
        // create order
        MvcResult result = mvc.perform(post("/wms/orders")
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .content(orderCreateContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();

        String orderId = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id").toString();
        Optional<WmsOrder> order = wmsOrderRepository.findOneById(orderId);
        MatcherAssert.assertThat(order.get().getStatus(), CoreMatchers.is(new OrderStatus(OrderStatus.Type.Submitted)));

        // update order
        when(currentUser.getRole()).thenReturn(String.valueOf(Role.PlatformAdmin));
        when(currentUser.getPrivileges()).thenReturn(new HashSet<Privilege>() {
            {
                add(Privilege.of(Privilege.Type.WmsOrderAccessPrivilege));
            }
        });

        String updateOrderContent = loadFixture("update_wms_order.json");
        DocumentContext content = JsonPath.parse(updateOrderContent)
                .set("$.effectiveFrom", LocalDate.now(ConfigProperties.getInstance().getTimeZone()).toString())
                .set("$.effectiveTo", LocalDate.now(ConfigProperties.getInstance().getTimeZone()).plusYears(1).toString());

        mvc.perform(post("/wms/orders/{id}", orderId)
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .content(content.jsonString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        order = Optional.of(wmsOrderRepository.getOne(orderId));
        MatcherAssert.assertThat(order.get().getStatus(), is(new OrderStatus(OrderStatus.Type.Audited)));

        order = Optional.of(wmsOrderRepository.getOne(orderId));

        MatcherAssert.assertThat(order.get().getStatus(), is(new OrderStatus(OrderStatus.Type.Audited)));
    }

    @Test
    public void shouldProcessWmsReNewOrder() throws Exception {
        WmsOrder newOrder = generateWmsOrder();
        DocumentContext createContent = JsonPath.parse(orderCreateContent)
                .set("$.type", WmsOrderType.Type.Renew);

        MvcResult result = mvc.perform(post("/wms/orders")
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .content(createContent.jsonString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(status().isCreated())
                .andReturn();


        String orderId = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id").toString();
        Optional<WmsOrder> order = Optional.of(wmsOrderRepository.getOne(orderId));
        MatcherAssert.assertThat(order.get().getStatus(), is(new OrderStatus(OrderStatus.Type.Submitted)));
        assertThat(order.get().getSubscriptionId(), is(newOrder.getSubscriptionId()));

        when(currentUser.getRole()).thenReturn(String.valueOf(Role.PlatformAdmin));
        when(currentUser.getPrivileges()).thenReturn(new HashSet<Privilege>() {
            {
                add(Privilege.of(Privilege.Type.WmsOrderAccessPrivilege));
            }
        });


        String updateOrderContent = loadFixture("update_wms_order.json");
        DocumentContext content = JsonPath.parse(updateOrderContent)
                .set("$.effectiveFrom", LocalDate.now(ConfigProperties.getInstance().getTimeZone()).toString())
                .set("$.effectiveTo", LocalDate.now(ConfigProperties.getInstance().getTimeZone()).plusYears(1).toString());
        mvc.perform(post("/wms/orders/{id}", orderId)
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .content(content.jsonString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        order = Optional.of(wmsOrderRepository.getOne(orderId));
        MatcherAssert.assertThat(order.get().getStatus(), is(new OrderStatus(OrderStatus.Type.Audited)));

        when(currentUser.getRole()).thenReturn(String.valueOf(Role.PlatformAdmin));
        when(currentUser.getPrivileges()).thenReturn(new HashSet<Privilege>() {
            {
                add(Privilege.of(Privilege.Type.WmsOrderAccessPrivilege));
            }
        });

        order = Optional.of(wmsOrderRepository.getOne(orderId));
        MatcherAssert.assertThat(order.get().getStatus(), is(new OrderStatus(OrderStatus.Type.Audited)));
    }

    @Test
    public void shouldProcessWmsReChargingOrder() throws Exception {
        WmsOrder newOrder = generateWmsOrder();
        DocumentContext createContent = JsonPath.parse(orderCreateContent)
                .set("$.type", WmsOrderType.Type.Recharge);

        MvcResult result = mvc.perform(post("/wms/orders")
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .content(createContent.jsonString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(status().isCreated())
                .andReturn();


        String orderId = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id").toString();
        Optional<WmsOrder> order = Optional.of(wmsOrderRepository.getOne(orderId));
        MatcherAssert.assertThat(order.get().getStatus(), is(new OrderStatus(OrderStatus.Type.Submitted)));
        assertThat(order.get().getSubscriptionId(), is(newOrder.getSubscriptionId()));

        when(currentUser.getRole()).thenReturn(String.valueOf(Role.PlatformAdmin));
        when(currentUser.getPrivileges()).thenReturn(new HashSet<Privilege>() {
            {
                add(Privilege.of(Privilege.Type.WmsOrderAccessPrivilege));
            }
        });
        String updateOrderContent = loadFixture("update_wms_order.json");
        DocumentContext content = JsonPath.parse(updateOrderContent)
                .set("$.effectiveFrom", LocalDate.now(ConfigProperties.getInstance().getTimeZone()).toString())
                .set("$.effectiveTo", LocalDate.now(ConfigProperties.getInstance().getTimeZone()).plusYears(1).toString());
        mvc.perform(post("/wms/orders/{id}", orderId)
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .content(content.jsonString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        order = Optional.of(wmsOrderRepository.getOne(orderId));
        MatcherAssert.assertThat(order.get().getStatus(), is(new OrderStatus(OrderStatus.Type.Audited)));

        order = Optional.of(wmsOrderRepository.getOne(orderId));
        MatcherAssert.assertThat(order.get().getStatus(), is(new OrderStatus(OrderStatus.Type.Audited)));
    }

    private WmsOrder generateWmsOrder() {
        return generateWmsOrder(wmsOrder -> {
        });
    }

    private WmsOrder generateWmsOrder(Consumer<WmsOrder> orderModifier) {
        WmsOrder wmsOrder = new WmsOrder();
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
        wmsOrder.setUserRole("EnterpriseAdmin");
        wmsOrder.setEnterpriseId("enterpriseId");
        wmsOrder.setVendor("wms");
        orderModifier.accept(wmsOrder);
        wmsOrder = wmsOrderRepository.save(wmsOrder);
        return wmsOrder;
    }


}

