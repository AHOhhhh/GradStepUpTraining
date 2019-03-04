package fun.hercules.order.order.business.acg.service;

import fun.hercules.order.order.JUnitWebAppTest;
import fun.hercules.order.order.business.acg.client.AcgBusinessClient;
import fun.hercules.order.order.business.acg.domain.Goods;
import fun.hercules.order.order.business.acg.domain.PriceRequest;
import fun.hercules.order.order.business.acg.domain.PriceResponse;
import fun.hercules.order.order.business.acg.dto.price.AcgGetPriceResponse;
import fun.hercules.order.order.business.acg.validation.AcgOrderValidator;
import fun.hercules.order.order.common.dto.Contact;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.MockitoAnnotations.initMocks;


@RunWith(MockitoJUnitRunner.Silent.class)
@JUnitWebAppTest
public class AcgIntegrationServiceTest {

    private AcgIntegrationService acgIntegrationService;
    @Mock
    private AcgOrderValidator validator;
    @Mock
    private AcgBusinessClient businessClient;

    @Before
    public void setUp() {
        acgIntegrationService = new AcgIntegrationService(businessClient, validator);
    }

    private PriceRequest getRequestData(Goods.Weight weight, Contact pickUpAddress, Contact dropOffAddress) {

        List<Goods> goodsList = new ArrayList<>();
        Goods goods = new Goods(weight,
                new Goods.Size(100D,100D,99.5,"CM"),
                10,
                null);
        goodsList.add(goods);

        PriceRequest priceRequest = new PriceRequest();
        PriceRequest.Airport fromAirport = new PriceRequest.Airport("1", false);
        PriceRequest.Airport toAirport = new PriceRequest.Airport("2", false);

        priceRequest.setGoods(goodsList);
        priceRequest.setDeparture(fromAirport);
        priceRequest.setArrival(toAirport);
        priceRequest.setPickUpAddress(pickUpAddress);
        priceRequest.setDropOffAddress(dropOffAddress);

        return priceRequest;
    }

    private AcgGetPriceResponse getCostResponseData() {
        AcgGetPriceResponse priceResponse = new AcgGetPriceResponse();

        priceResponse.setDropOffPrice(BigDecimal.TEN);
        priceResponse.setPickUpPrice(new BigDecimal(20));
        priceResponse.setUnitPriceByVolume(new BigDecimal(30));
        priceResponse.setUnitPriceByWeight(BigDecimal.ONE);

        return priceResponse;
    }

    @Test
    public void get_order_price_only_have_unit_price() {
        //given
        Goods.Weight weight = new Goods.Weight(BigDecimal.TEN, "KG");
        PriceRequest request = getRequestData(weight, new Contact(), new Contact());
        AcgGetPriceResponse costResponseData = getCostResponseData();

        given(businessClient.getPrice(any())).willReturn(costResponseData);

        //when
        PriceResponse result = acgIntegrationService.getOrderPrice(request);

        //then
        assertThat(result.getDropOffFee()).isEqualTo(BigDecimal.ZERO);
        assertThat(result.getPickUpFee()).isEqualTo(BigDecimal.ZERO);
        assertThat(result.getTotal().setScale(1, BigDecimal.ROUND_DOWN)).isEqualTo(new BigDecimal("298.5"));
    }

    @Test
    public void get_order_price_have_unit_price_and_pickup_price() {
        //given
        Goods.Weight weight = new Goods.Weight(BigDecimal.TEN, "KG");
        Contact pickUpAddress = new Contact();
        pickUpAddress.setId("12");
        PriceRequest request = getRequestData(weight, pickUpAddress, new Contact());
        AcgGetPriceResponse costResponseData = getCostResponseData();

        given(businessClient.getPrice(any())).willReturn(costResponseData);

        //when
        PriceResponse result = acgIntegrationService.getOrderPrice(request);

        //then
        assertThat(result.getDropOffFee()).isEqualTo(BigDecimal.ZERO);
        assertThat(result.getPickUpFee()).isEqualTo(new BigDecimal(20));
        assertThat(result.getTotal().setScale(1, BigDecimal.ROUND_DOWN)).isEqualTo(new BigDecimal("318.5"));
    }

    @Test
    public void get_order_price_have_unit_price_pickup_price_and_drop_off_price() {
        //given
        Contact pickUpAddress = new Contact();
        Contact dropOffAddress = new Contact();
        pickUpAddress.setId("12");
        dropOffAddress.setId("13");
        Goods.Weight weight = new Goods.Weight(BigDecimal.TEN, "KG");
        PriceRequest request = getRequestData(weight, pickUpAddress, dropOffAddress);
        AcgGetPriceResponse costResponseData = getCostResponseData();

        given(businessClient.getPrice(any())).willReturn(costResponseData);

        //when
        PriceResponse result = acgIntegrationService.getOrderPrice(request);

        //then
        assertThat(result.getDropOffFee()).isEqualTo(BigDecimal.TEN);
        assertThat(result.getPickUpFee()).isEqualTo(new BigDecimal(20));
        assertThat(result.getTotal().setScale(1, BigDecimal.ROUND_DOWN)).isEqualTo(new BigDecimal("328.5"));
    }

    @Test
    public void get_order_price_only_have_unit_price_weight_higher() {
        //given
        Goods.Weight weight = new Goods.Weight(BigDecimal.valueOf(100), "KG");
        PriceRequest request = getRequestData(weight, new Contact(),new Contact());
        AcgGetPriceResponse cost = getCostResponseData();

        given(businessClient.getPrice(any())).willReturn(cost);

        //when
        PriceResponse result = acgIntegrationService.getOrderPrice(request);

        //then
        assertThat(result.getDropOffFee()).isEqualTo(BigDecimal.ZERO);
        assertThat(result.getPickUpFee()).isEqualTo(BigDecimal.ZERO);
        assertThat(result.getTotal()).isEqualTo(new BigDecimal("1000"));
    }
}