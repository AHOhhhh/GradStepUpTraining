package fun.hercules.order.order.business.wms;

import fun.hercules.order.order.business.wms.domain.ChargingRule;
import fun.hercules.order.order.business.wms.domain.WmsOrder;
import fun.hercules.order.order.business.wms.validators.WmsOrderValidator;
import fun.hercules.order.order.platform.order.model.OrderStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class WmsOrderValidatorTest {

    private WmsOrderValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new WmsOrderValidator();
    }

    @Test
    public void shouldThrowExceptionWhenMinPriceGreaterThanMaxPrice() throws Exception {
        WmsOrder order = generateWmsOrder();
        order.setMinPrice(10);
        order.setMaxPrice(1);
        List<ChargingRule> rules = order.getChargingRules();
        rules.add(new ChargingRule(1, null, BigDecimal.valueOf(0.12)));
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        when(context.buildConstraintViolationWithTemplate(argument.capture()))
                .thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));

        assertFalse(validator.isValid(order, context));
        assertThat("Max price should greater than mix price. ", is(argument.getAllValues().get(0)));
    }

    @Test
    public void shouldThrowExceptionWhenEffectiveFromIsAfterEffectiveTo() throws Exception {
        WmsOrder order = generateWmsOrder();
        List<ChargingRule> rules = order.getChargingRules();
        rules.add(new ChargingRule(1, null, BigDecimal.valueOf(0.12)));
        order.setEffectiveFrom(parseLocalDate("2017-11-13"));
        order.setEffectiveTo(parseLocalDate("2016-11-13"));

        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        when(context.buildConstraintViolationWithTemplate(argument.capture()))
                .thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));

        assertFalse(validator.isValid(order, context));
        assertThat("EffectiveFrom should before effectiveTo. ", is(argument.getAllValues().get(0)));
    }

    @Test
    public void shouldThrowExceptionWhenEffectiveFromIsAfterCreateAt() throws Exception {
        WmsOrder order = generateWmsOrder();
        List<ChargingRule> rules = order.getChargingRules();
        rules.add(new ChargingRule(1, null, BigDecimal.valueOf(0.12)));
        order.setCreatedAt(Instant.parse("2016-12-13T00:00:00Z"));
        order.setEffectiveFrom(parseLocalDate("2016-11-13"));
        order.setEffectiveTo(parseLocalDate("2017-11-12"));

        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        when(context.buildConstraintViolationWithTemplate(argument.capture()))
                .thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));

        assertFalse(validator.isValid(order, context));
        assertThat("EffectiveFrom should after created date. ", is(argument.getAllValues().get(0)));
    }

    @Test
    public void shouldThrowExceptionWhenChargingRulesAmountGreaterThanTen() throws Exception {
        WmsOrder order = generateWmsOrder();
        List<ChargingRule> rules = order.getChargingRules();
        rules.add(new ChargingRule(1, 10, BigDecimal.valueOf(1.2)));
        rules.add(new ChargingRule(10, 20, BigDecimal.valueOf(1.2)));
        rules.add(new ChargingRule(20, 30, BigDecimal.valueOf(1.2)));
        rules.add(new ChargingRule(30, 40, BigDecimal.valueOf(1.2)));
        rules.add(new ChargingRule(40, 50, BigDecimal.valueOf(1.2)));
        rules.add(new ChargingRule(50, 60, BigDecimal.valueOf(1.2)));
        rules.add(new ChargingRule(60, 70, BigDecimal.valueOf(1.2)));
        rules.add(new ChargingRule(70, 80, BigDecimal.valueOf(1.2)));
        rules.add(new ChargingRule(80, 90, BigDecimal.valueOf(1.2)));
        rules.add(new ChargingRule(90, 100, BigDecimal.valueOf(1.2)));
        rules.add(new ChargingRule(100, null, BigDecimal.valueOf(1.2)));

        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        when(context.buildConstraintViolationWithTemplate(argument.capture()))
                .thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));

        assertFalse(validator.isValid(order, context));
        assertThat("Max amount of charging rule is 10. ", is(argument.getAllValues().get(0)));
    }

    @Test
    public void shouldThrowExceptionWhenMinQualityGreaterThanMaxQuality() throws Exception {
        WmsOrder order = generateWmsOrder();
        List<ChargingRule> rules = order.getChargingRules();
        rules.add(new ChargingRule(1, 10, BigDecimal.valueOf(0.12)));
        rules.add(new ChargingRule(10, 8, BigDecimal.valueOf(0.12)));
        rules.add(new ChargingRule(11, null, BigDecimal.valueOf(0.12)));

        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        when(context.buildConstraintViolationWithTemplate(argument.capture()))
                .thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));

        assertFalse(validator.isValid(order, context));
        assertThat("QuantityTo should greater than QuantityFrom. QuantityFrom should continue with last QuantityTo. ", is(argument.getAllValues().get(0)));
    }

    @Test
    public void shouldThrowExceptionWhenMinQualityLessThanLastMaxQuality() throws Exception {
        WmsOrder order = generateWmsOrder();
        List<ChargingRule> rules = order.getChargingRules();
        rules.add(new ChargingRule(1, 100, BigDecimal.valueOf(0.12)));
        rules.add(new ChargingRule(110, null, BigDecimal.valueOf(0.12)));

        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        when(context.buildConstraintViolationWithTemplate(argument.capture()))
                .thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));

        assertFalse(validator.isValid(order, context));
        assertThat("QuantityFrom should continue with last QuantityTo. ", is(argument.getAllValues().get(0)));
    }

    @Test
    public void shouldThrowExceptionWhenUnitPriceIsLessThanZero() throws Exception {
        WmsOrder order = generateWmsOrder();
        List<ChargingRule> rules = order.getChargingRules();
        rules.add(new ChargingRule(1, null, BigDecimal.valueOf(-0.9)));

        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        when(context.buildConstraintViolationWithTemplate(argument.capture()))
                .thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));

        assertFalse(validator.isValid(order, context));
        assertThat("Unit price should be a positive number. ", is(argument.getAllValues().get(0)));
    }

    @Test
    public void shouldThrowExceptionWhenApprovedPriceLessThanMinPrice() throws Exception {
        WmsOrder order = generateWmsOrder();
        order.setApprovedPrice(BigDecimal.valueOf(1));
        List<ChargingRule> rules = order.getChargingRules();
        rules.add(new ChargingRule(1, null, BigDecimal.valueOf(0.12)));

        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        when(context.buildConstraintViolationWithTemplate(argument.capture()))
                .thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));

        assertFalse(validator.isValid(order, context));
        assertThat("ApprovedPrice should greater than minPrice. ", is(argument.getAllValues().get(0)));
    }

    @Test
    public void shouldThrowExceptionWhenMinPriceIsNull() throws Exception {
        WmsOrder order = generateWmsOrder();
        order.setMinPrice(null);
        List<ChargingRule> rules = order.getChargingRules();
        rules.add(new ChargingRule(1, null, BigDecimal.valueOf(0.12)));

        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        when(context.buildConstraintViolationWithTemplate(argument.capture()))
                .thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));

        assertFalse(validator.isValid(order, context));
        assertThat("Min price should not be null. ", is(argument.getAllValues().get(0)));
    }

    @Test
    public void shouldPass() throws Exception {
        WmsOrder order = generateWmsOrder();
        List<ChargingRule> rules = order.getChargingRules();

        rules.add(new ChargingRule(30, null, BigDecimal.valueOf(1.2)));
        rules.add(new ChargingRule(10, 20, BigDecimal.valueOf(1.2)));
        rules.add(new ChargingRule(1, 10, BigDecimal.valueOf(1.2)));
        rules.add(new ChargingRule(20, 30, BigDecimal.valueOf(1.2)));


        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        when(context.buildConstraintViolationWithTemplate(argument.capture()))
                .thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));

        assertTrue(validator.isValid(order, context));
    }


    private WmsOrder generateWmsOrder() {
        WmsOrder order = new WmsOrder();
        order.setStatus(new OrderStatus(OrderStatus.Type.Audited));
        order.setMaxPrice(100);
        order.setMinPrice(10);
        order.setEffectiveFrom(parseLocalDate("2117-11-13"));
        order.setEffectiveTo(parseLocalDate("2118-11-13"));
        order.setCreatedAt(Instant.EPOCH);
        order.setEnterpriseShortName("Thoughtworks");

        ChargingRule rule = new ChargingRule();
        rule.setQuantityFrom(0);
        rule.setQuantityTo(1);
        rule.setUnitPrice(BigDecimal.valueOf(0.12));
        ArrayList<ChargingRule> chargingRules = new ArrayList<>();
        chargingRules.add(rule);

        order.setChargingRules(chargingRules);
        order.setApprovedPrice(BigDecimal.valueOf(30));
        return order;
    }

    private LocalDate parseLocalDate(String date) {
        return LocalDate.parse(date);
    }

}
