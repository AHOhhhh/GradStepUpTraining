package fun.hercules.order.order.business.acg;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import fun.hercules.order.order.JUnitWebAppTest;
import fun.hercules.order.order.business.acg.domain.AcgAirport;
import fun.hercules.order.order.business.acg.domain.PriceRequest;
import fun.hercules.order.order.business.acg.repository.AirportRepository;
import fun.hercules.order.order.business.acg.validation.AcgOrderValidator;
import fun.hercules.order.order.common.exceptions.BadRequestException;
import fun.hercules.order.order.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static fun.hercules.order.order.utils.ResourceUtils.loadFixture;

@Slf4j
@Transactional
@RunWith(SpringRunner.class)
@JUnitWebAppTest
public class PriceValidationTest {
    @Autowired
    private AirportRepository airportRepository;

    private AcgAirport airportOne;

    private AcgAirport airportTwo;

    private AcgOrderValidator validation;

    DocumentContext content;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        content = JsonPath.parse(loadFixture("acg/integration/get_order_price.json"));
        validation = new AcgOrderValidator(airportRepository);
        airportOne = generateAirports("AAA");
        airportTwo = generateAirports("BBB");
    }

    @Test
    public void shouldThrowExceptionWhenArrivalNotProvidePickUp() throws Exception {
        content.set("$.departure.airportId", airportOne.getAirportId())
                .set("$.arrival.airportId", airportTwo.getAirportId())
                .set("$.arrival.delivery", true);

        PriceRequest request = JsonUtils.getMapper().readValue(content.jsonString(), PriceRequest.class);
        thrown.expect(BadRequestException.class);
        thrown.expectMessage("Airport BBB have no drop off service");
        validation.validatePriceRequest(request);
    }

    @Test
    public void shouldThrowExceptionWhenMissingPhoneNumber() throws Exception {
        content.set("$.departure.airportId", airportOne.getAirportId())
                .set("$.arrival.airportId", airportTwo.getAirportId())
                .set("$.pickUpAddress.cellphone", "");

        PriceRequest request = JsonUtils.getMapper().readValue(content.jsonString(), PriceRequest.class);
        thrown.expect(BadRequestException.class);
        thrown.expectMessage("missing mobile phone number");
        validation.validatePriceRequest(request);
    }

    @Test
    public void shouldThrowExceptionWhenAirportNotExist() throws Exception {
        PriceRequest request = JsonUtils.getMapper().readValue(content.jsonString(), PriceRequest.class);
        request.setDeparture(new PriceRequest.Airport("GZH", true));
        thrown.expect(BadRequestException.class);
        thrown.expectMessage("Have no this airport GZH");
        validation.validatePriceRequest(request);
    }

    @Test
    public void shouldThrowExceptionWhenPickUpAddressIsNUll() throws Exception {
        content.set("$.departure.airportId", airportOne.getAirportId())
                .set("$.arrival.airportId", airportTwo.getAirportId())
                .set("$.pickUpAddress", null);

        PriceRequest request = JsonUtils.getMapper().readValue(content.jsonString(), PriceRequest.class);
        thrown.expect(BadRequestException.class);
        thrown.expectMessage("missing pick up address");
        validation.validatePriceRequest(request);
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
