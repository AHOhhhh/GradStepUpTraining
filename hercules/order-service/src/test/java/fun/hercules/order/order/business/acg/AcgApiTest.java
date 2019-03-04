package fun.hercules.order.order.business.acg;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import fun.hercules.order.order.JUnitWebAppTest;
import fun.hercules.order.order.business.acg.api.AcgIntegrationApi;
import fun.hercules.order.order.business.acg.domain.AcgAirport;
import fun.hercules.order.order.business.acg.domain.AcgOrder;
import fun.hercules.order.order.business.acg.dto.AcgResponse;
import fun.hercules.order.order.business.acg.dto.plan.AcgPlanRequest;
import fun.hercules.order.order.business.acg.dto.price.AcgGetPriceRequest;
import fun.hercules.order.order.business.acg.dto.price.AcgGetPriceResponse;
import fun.hercules.order.order.business.acg.repository.AirportRepository;
import fun.hercules.order.order.business.acg.service.AcgAirportService;
import fun.hercules.order.order.platform.order.model.PaymentRequest;
import fun.hercules.order.order.platform.order.repository.PaymentRequestRepository;
import fun.hercules.order.order.platform.user.Role;
import fun.hercules.order.order.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static fun.hercules.order.order.utils.ResourceUtils.loadFixture;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Transactional
@RunWith(SpringRunner.class)
@JUnitWebAppTest
public class AcgApiTest extends AcgOrderTestBase {
    @Autowired
    private AcgAirportService airportService;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private PaymentRequestRepository paymentRequestRepository;

    private AcgGetPriceResponse response;

    private AcgAirport airportOne;

    private AcgAirport airportTwo;

    @Autowired
    private AcgIntegrationApi acgIntegrationApi;

    @Before
    public void setUp() throws Exception {
        airportOne = generateAirports("AAA");
        airportTwo = generateAirports("BBB");
        response = new AcgGetPriceResponse();
        response.setDropOffPrice(BigDecimal.valueOf(100));
        response.setPickUpPrice(BigDecimal.valueOf(200));
        response.setUnitPriceByVolume(BigDecimal.valueOf(300));
        response.setUnitPriceByWeight(BigDecimal.valueOf(400));
        response.setStatus(AcgResponse.Status.SUCCESS);
    }

    @Test
    public void shouldReturnAllAirports() throws Exception {

        when(currentUser.getRole()).thenReturn(Role.EnterpriseUser.toString());

        mvc.perform(get("/acg-api/airports")
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser)))
                .andExpect(status().isOk());
        List<AcgAirport> airportList = airportService.list();

        assertThat(airportList.size(), is(2));
        assertThat(airportList.get(0).getAirportId(), is("AAA"));
        assertThat(airportList.get(1).getAirportId(), is("BBB"));
    }

    @Test
    public void shouldGetPrice() throws Exception {
        ArgumentCaptor<AcgGetPriceRequest> priceRequestArg = ArgumentCaptor.forClass(AcgGetPriceRequest.class);
        when(acgBusinessClient.getPrice(priceRequestArg.capture()))
                .thenReturn(response);

        String priceRequest = loadFixture("acg/integration/get_order_price.json");
        DocumentContext content = JsonPath.parse(priceRequest)
                .set("$.departure.airportId", airportOne.getAirportId())
                .set("$.arrival.airportId", airportTwo.getAirportId());
        mvc.perform(post("/acg-api/order-price")
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .content(content.jsonString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void shouldReturnPaymentRequest() throws Exception {
        when(currentUser.getRole()).thenReturn(Role.EnterpriseUser.toString());
        AcgOrder acgOrder = JsonUtils.getMapper().readValue(loadFixture("acg/integration/acg_order_with_id.json"), AcgOrder.class);
        acgOrderRepository.save(acgOrder);
        acgOrder = acgOrderRepository.findAll().get(0);
        String orderId = acgOrder.getId();
        String currentTime = "2014-12-03T10:15:30Z";
        PaymentRequest paymentRequest = new PaymentRequest(acgOrder, () -> Instant.parse(currentTime));
        paymentRequestRepository.save(paymentRequest);

        MvcResult result = mvc.perform(get("/acg-api/orders/" + orderId + "/payment-status")
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser)))
                .andExpect(status().isOk()).andReturn();
        DocumentContext parse = JsonPath.parse(result.getResponse().getContentAsString());

        assertThat(parse.read("$.orderId").toString(), is(orderId));
        assertThat(parse.read("$.amount").toString(), is("4800.0"));
        assertThat(parse.read("$.currency").toString(), is("CNY"));
    }

    @Test
    public void should_return_success_when__call_update_plan() throws Exception {
        AcgOrder acgOrder = JsonUtils.getMapper().readValue(loadFixture("acg/integration/acg_order_with_id.json"), AcgOrder.class);
        acgOrderRepository.save(acgOrder);
        acgOrder = acgOrderRepository.findAll().get(0);
        String orderId = acgOrder.getId();
        String planRequest = loadFixture("acg/integration/acg_plan_request.json");
        DocumentContext content = JsonPath.parse(planRequest);
        MvcResult mvcResult = mvc.perform(post("/acg-api/orders/" + orderId + "/plan")
                .header(HttpHeaders.AUTHORIZATION, generateAuthorization(currentUser))
                .content(content.jsonString())
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        DocumentContext parse = JsonPath.parse(mvcResult.getResponse().getContentAsString());
        assertThat(parse.read("$.status").toString(), is("SUCCESS"));

    }

    private AcgAirport generateAirports(String airportId) {
        AcgAirport airport = new AcgAirport();
        airport.setAirportName("airport-name-" + airportId);
        airport.setCity("city-" + airportId);
        airport.setAbroad(true);
        airport.setDelivery(false);
        airport.setPickup(true);
        airport.setAirportId(airportId);
        airport.setName("name-" + airportId);

        return airportRepository.save(airport);
    }
}
