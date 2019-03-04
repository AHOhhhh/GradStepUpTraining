package fun.hercules.order.order.business.wms.validators;

import fun.hercules.order.order.business.wms.domain.ChargingRule;
import fun.hercules.order.order.business.wms.domain.WmsOrder;
import fun.hercules.order.order.common.constants.ConfigProperties;
import fun.hercules.order.order.platform.order.model.OrderStatus;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class WmsOrderValidator implements ConstraintValidator<WmsOrderConstraint, WmsOrder> {

    private static final int MAX_AMOUNT_RULES = 10;
    public static final int MAX_VALUE = 99999999;

    @Override
    public void initialize(WmsOrderConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(WmsOrder order, ConstraintValidatorContext context) {
        boolean valid = true;
        if (order.getStatus().equals(new OrderStatus(OrderStatus.Type.Audited))) {
            String errors = validateMaxAndMinPrice(order)
                    + validateEffectiveDuration(order)
                    + validateChargingRule(order)
                    + validateApprovedPrice(order)
                    + validateEnterprise(order.getEnterpriseShortName());

            if (!StringUtils.isEmpty(errors)) {
                valid = false;
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(errors).addConstraintViolation();
            }
        }

        return valid;
    }

    private String validateEnterprise(String enterpriseShortName) {
        String errorMessage = "";
        if (StringUtils.isEmpty(enterpriseShortName)) {
            errorMessage = "EnterpriseShortName should not be null";
        }
        return  errorMessage;
    }

    private String validateMaxAndMinPrice(WmsOrder order) {
        String errorMessage = "";
        Integer minPrice = order.getMinPrice();
        if (null == minPrice) {
            errorMessage += "Min price should not be null. ";
        }

        if (!StringUtils.isEmpty(errorMessage)) {
            return errorMessage;
        }

        if (minPrice < 0) {
            errorMessage += "Min Price should be a positive number. ";
        }

        if (minPrice > MAX_VALUE) {
            errorMessage += String.format("Min Price should be less than %s. ", MAX_VALUE);
        }

        Integer maxPrice = order.getMaxPrice();
        if (maxPrice != null) {
            if (maxPrice < 0) {
                errorMessage += "Max Price should be a positive number. ";
            }

            if (maxPrice < minPrice) {
                errorMessage += "Max price should greater than mix price. ";
            }

            if (maxPrice > MAX_VALUE) {
                errorMessage += String.format("Max Price should be less than %s. ", MAX_VALUE);
            }
        }

        return errorMessage;
    }

    private String validateChargingRule(WmsOrder order) {
        StringBuilder errorMessage = new StringBuilder();
        List<ChargingRule> rules = order.getChargingRules();

        if (rules.size() <= 0) {
            return "Charging rule should not be null. ";
        }

        if (rules.size() > MAX_AMOUNT_RULES) {
            errorMessage = errorMessage.append("Max amount of charging rule is " + MAX_AMOUNT_RULES + ". ");
        }

        List<ChargingRule> sortedRules = rules.stream()
                .sorted(Comparator.comparing(ChargingRule::getQuantityFrom))
                .collect(Collectors.toList());

        Integer lastMinQuality = 0;
        int index = 0;
        for (ChargingRule rule : sortedRules) {
            index ++;

            if (index == 1 && rule.getQuantityFrom() != 0) {
                errorMessage.append("The first quantityFrom must be zero");
            }

            if (index != 1 && rule.getQuantityFrom() < 0) {
                errorMessage.append("QuantityFrom should be a positive number. ");
            }

            if (index == sortedRules.size() && rule.getQuantityTo() != null) {
                errorMessage.append("The last quantiryTo must be null");
            }

            if (index != sortedRules.size() && rule.getQuantityTo() < 0) {
                errorMessage.append("QuantityTo should be a positive number. ");
            }

            if (index != sortedRules.size() && rule.getQuantityFrom() >= rule.getQuantityTo()) {
                errorMessage.append("QuantityTo should greater than QuantityFrom. ");
            }

            if (rule.getUnitPrice().compareTo(BigDecimal.valueOf(0)) <= 0) {
                errorMessage.append("Unit price should be a positive number. ");
            }

            if (index != 1 && !Objects.equals(rule.getQuantityFrom(), lastMinQuality)) {
                errorMessage.append("QuantityFrom should continue with last QuantityTo. ");
            }

            lastMinQuality = rule.getQuantityTo();
        }
        return errorMessage.toString();
    }

    private String validateEffectiveDuration(WmsOrder order) {
        String errorMessage = "";

        LocalDate effectiveFrom = order.getEffectiveFrom();
        LocalDate effectiveTo = order.getEffectiveTo();

        if (null == effectiveFrom) {
            errorMessage += "EffectiveFrom and effectiveTo should not be null. ";
        }

        if (null == effectiveTo) {
            errorMessage += "EffectiveTo and effectiveTo should not be null. ";
        }

        if (!StringUtils.isEmpty(errorMessage)) {
            return errorMessage;
        }

        if (effectiveFrom.isAfter(effectiveTo)) {
            errorMessage += "EffectiveFrom should before effectiveTo. ";
        }

        if (order.getEffectiveFrom().isBefore(order.getCreatedAt().atZone(ConfigProperties.getInstance().getTimeZone()).toLocalDate())) {
            errorMessage += "EffectiveFrom should after created date. ";
        }
        return errorMessage;
    }

    private String validateApprovedPrice(WmsOrder order) {
        String errorMessage = "";
        BigDecimal approvedPrice = order.getApprovedPrice();
        if (null == approvedPrice) {
            return "ApprovedPrice should not be null. ";
        }

        Integer minPrice = order.getMinPrice();
        if (null != minPrice && approvedPrice.compareTo(BigDecimal.valueOf(minPrice)) < 0) {
            errorMessage = "ApprovedPrice should greater than minPrice. ";
        }

        if (approvedPrice.compareTo(BigDecimal.ZERO) > 0 && approvedPrice.compareTo(BigDecimal.valueOf(MAX_VALUE)) > 0) {
            errorMessage += String.format("ApprovedPrice should be less than %s. ", MAX_VALUE);
        }
        return errorMessage;
    }

}
