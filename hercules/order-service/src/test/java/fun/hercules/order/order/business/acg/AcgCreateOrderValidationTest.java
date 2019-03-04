package fun.hercules.order.order.business.acg;

import fun.hercules.order.order.JUnitWebAppTest;
import fun.hercules.order.order.business.acg.domain.AcgOrder;
import fun.hercules.order.order.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;

import static fun.hercules.order.order.utils.ResourceUtils.loadFixture;

@Slf4j
@Transactional
@RunWith(SpringRunner.class)
@JUnitWebAppTest
public class AcgCreateOrderValidationTest extends AcgOrderTestBase {
    @Test
    public void shouldCreateAcgOrderWithValidData() throws Exception {
        acgOrderService.create(getOrderFixture());
    }

    @Test
    public void shouldAllowEmptyPickUpAndDropOffAddressWhenDeliveryIsFalse() throws Exception {
        AcgOrder order = getOrderFixture();
        order.getShippingInfo().getDeparture().setDelivery(false);
        order.getShippingInfo().getArrival().setDelivery(false);
        order.getShippingInfo().setPickUpAddress(null);
        order.getShippingInfo().setDropOffAddress(null);
        acgOrderService.create(order);
    }

    @Test
    public void shouldNotAllowValidatePickUpAddressWhenDepartureDeliveryEnabled() throws Exception {
        AcgOrder order = getOrderFixture();
        order.getShippingInfo().getDeparture().setDelivery(true);
        order.getShippingInfo().setPickUpAddress(null);
        assertThrows(() -> acgOrderService.create(order), ConstraintViolationException.class);
    }

    @Test
    public void shouldNotAllowValidateDropOffAddressWhenArrivalDeliveryEnabled() throws Exception {
        AcgOrder order = getOrderFixture();
        order.getShippingInfo().getDeparture().setDelivery(true);
        order.getShippingInfo().setPickUpAddress(null);
        assertThrows(() -> acgOrderService.create(order), ConstraintViolationException.class);
    }

    @Test
    public void shouldValidateShippingInfoEstimatedDeliveryTime() throws Exception {
        assertThrows(() -> {
            AcgOrder order = getOrderFixture();
            order.getShippingInfo().setEstimatedDeliveryTime(null);
            acgOrderService.create(order);
        }, ConstraintViolationException.class);
    }

    @Test
    public void shouldValidateShippingInfoAirportInfoTime() throws Exception {
        assertThrows(() -> {
            AcgOrder order = getOrderFixture();
            order.getShippingInfo().setArrival(null);
            acgOrderService.create(order);
        }, ConstraintViolationException.class);

        assertThrows(() -> {
            AcgOrder order = getOrderFixture();
            order.getShippingInfo().setDeparture(null);
            acgOrderService.create(order);
        }, ConstraintViolationException.class);

        assertThrows(() -> {
            AcgOrder order = getOrderFixture();
            order.getShippingInfo().getArrival().setAirportId("XX");
            acgOrderService.create(order);
        }, ConstraintViolationException.class);

        assertThrows(() -> {
            AcgOrder order = getOrderFixture();
            order.getShippingInfo().getDeparture().setAirportId("XX");
            acgOrderService.create(order);
        }, ConstraintViolationException.class);
    }

    @Test
    public void shouldValidateGoodsCount() throws Exception {
        assertThrows(() -> {
            AcgOrder order = getOrderFixture();
            order.getGoods().clear();
            acgOrderService.create(order);
        }, ConstraintViolationException.class);

        assertThrows(() -> {
            AcgOrder order = getOrderFixture();
            order.setGoods(null);
            acgOrderService.create(order);
        }, ConstraintViolationException.class);
    }

    @Test
    public void shouldValidatePrice() throws Exception {
        assertThrows(() -> {
            AcgOrder order = getOrderFixture();
            order.setPrice(null);
            acgOrderService.create(order);
        }, ConstraintViolationException.class);

        assertThrows(() -> {
            AcgOrder order = getOrderFixture();
            order.getPrice().setAirlineFee(BigDecimal.valueOf(0));
            acgOrderService.create(order);
        }, ConstraintViolationException.class);
        assertThrows(() -> {
            AcgOrder order = getOrderFixture();
            order.getPrice().setAirlineFee(BigDecimal.valueOf(-1));
            acgOrderService.create(order);
        }, ConstraintViolationException.class);
        assertThrows(() -> {
            AcgOrder order = getOrderFixture();
            order.getPrice().setDropOffFee(BigDecimal.valueOf(0));
            acgOrderService.create(order);
        }, ConstraintViolationException.class);
    }

    @Test
    public void shouldValidateCurrency() throws Exception {
        assertThrows(() -> {
            AcgOrder order = getOrderFixture();
            order.setCurrency(null);
            acgOrderService.create(order);
        }, ConstraintViolationException.class);
    }

    @Test
    public void shouldValidateContact() throws Exception {
        assertThrows(() -> {
            AcgOrder order = getOrderFixture();
            order.setContact(null);
            acgOrderService.create(order);
        }, ConstraintViolationException.class);
    }

    @Test
    public void shouldValidateGoodsContent() throws Exception {
        assertThrows(() -> {
            AcgOrder order = getOrderFixture();
            order.getGoods().get(0).setDeclarationInfo(RandomStringUtils.randomNumeric(101));
            acgOrderService.create(order);
        }, ConstraintViolationException.class);

        assertThrows(() -> {
            AcgOrder order = getOrderFixture();
            order.getGoods().get(0).setName("");
            acgOrderService.create(order);
        }, ConstraintViolationException.class);
        assertThrows(() -> {
            AcgOrder order = getOrderFixture();
            order.getGoods().get(0).setPrice(BigDecimal.valueOf(0));
            acgOrderService.create(order);
        }, ConstraintViolationException.class);
        assertThrows(() -> {
            AcgOrder order = getOrderFixture();
            order.getGoods().get(0).setPackageQuantity(0);
            acgOrderService.create(order);
        }, ConstraintViolationException.class);
        assertThrows(() -> {
            AcgOrder order = getOrderFixture();
            order.getGoods().get(0).setQuantity(0);
            acgOrderService.create(order);
        }, ConstraintViolationException.class);
        assertThrows(() -> {
            AcgOrder order = getOrderFixture();
            order.getGoods().get(0).setSize(null);
            acgOrderService.create(order);
        }, ConstraintViolationException.class);
        assertThrows(() -> {
            AcgOrder order = getOrderFixture();
            order.getGoods().get(0).setWeight(null);
            acgOrderService.create(order);
        }, ConstraintViolationException.class);
        assertThrows(() -> {
            AcgOrder order = getOrderFixture();
            order.getGoods().get(0).getSize().setHeight(BigDecimal.valueOf(0));
            acgOrderService.create(order);
        }, ConstraintViolationException.class);
        assertThrows(() -> {
            AcgOrder order = getOrderFixture();
            order.getGoods().get(0).getWeight().setValue(BigDecimal.valueOf(0));
            acgOrderService.create(order);
        }, ConstraintViolationException.class);
    }

    private AcgOrder getOrderFixture() {
        AcgOrder order = JsonUtils.unmarshal(loadFixture("acg/create_acg_order.json"), AcgOrder.class);
        order.setUserId("test-user-id");
        order.setEnterpriseId("test-enterprise-id");
        return order;
    }
}
