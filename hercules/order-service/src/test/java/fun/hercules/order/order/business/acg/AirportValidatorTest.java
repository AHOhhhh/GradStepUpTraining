package fun.hercules.order.order.business.acg;

import fun.hercules.order.order.business.acg.domain.AcgAirport;
import fun.hercules.order.order.business.acg.validation.AirportValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.validation.ConstraintValidatorContext;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AirportValidatorTest {
    private AirportValidator airportValidator;

    @Before
    public void setUp() throws Exception {
        airportValidator = new AirportValidator();
    }

    @Test
    public void shouldPass() {
        AcgAirport acgAirport = generateAirports("PEK");
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        when(context.buildConstraintViolationWithTemplate(argument.capture()))
                .thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));

        assertTrue(airportValidator.isValid(acgAirport, context));
    }

    @Test
    public void shouldThrowExceptionWhenAirportIdLengthIsNotThree() {
        AcgAirport acgAirport = generateAirports("TOOLONG");
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        when(context.buildConstraintViolationWithTemplate(argument.capture()))
                .thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));

        assertFalse(airportValidator.isValid(acgAirport, context));
        assertThat("AcgAirport id length should be 3.", is(argument.getAllValues().get(0)));
    }

    @Test
    public void shouldThrowExceptionWhenAirportIdIsEmpty() {
        AcgAirport acgAirport = generateAirports("");
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        when(context.buildConstraintViolationWithTemplate(argument.capture()))
                .thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));

        assertFalse(airportValidator.isValid(acgAirport, context));
        assertThat("AcgAirport id length should be 3.", is(argument.getAllValues().get(0)));
    }
    
    private AcgAirport generateAirports(String airportId) {
        AcgAirport airport = new AcgAirport();
        airport.setAirportName("airport-name-" + airportId);
        airport.setCity("city-" + airportId);
        airport.setAbroad(true);
        airport.setDelivery(true);
        airport.setPickup(true);
        airport.setAirportId(airportId);
        airport.setName("name-" + airportId);

        return airport;
    }
}
