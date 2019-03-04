package fun.hercules.order.order.business.acg.validation;

import fun.hercules.order.order.business.acg.domain.AcgAirport;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class AirportValidator implements ConstraintValidator<AirportConstraint, AcgAirport> {

    @Override
    public void initialize(AirportConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(AcgAirport airport, ConstraintValidatorContext context) {
        boolean valid = true;
        String errors = validateAirportId(airport);

        if (!StringUtils.isEmpty(errors)) {
            valid = false;
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errors).addConstraintViolation();
        }

        return valid;
    }

    private String validateAirportId(AcgAirport airport) {
        String errorMessage = "";
        String airportId = Optional.ofNullable(airport.getAirportId()).orElse("");
        if (airportId.length() != 3) {
            errorMessage += "AcgAirport id length should be 3.";
        }

        return errorMessage;
    }
}
