package fun.hercules.order.order.business.wms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import fun.hercules.order.order.business.wms.domain.ChargingRule;
import fun.hercules.order.order.business.wms.domain.WmsOrder;
import fun.hercules.order.order.business.wms.domain.WmsOrderType;
import fun.hercules.order.order.common.constants.DateTimeConstants;
import fun.hercules.order.order.common.dto.Contact;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.YEARS;

@Data
@ToString
public class WmsOpenRequest {

    String orderId;

    String subscriptionId;

    String uid;

    String eid;

    Integer productId;

    int numberOfYears;

    @JsonFormat(pattern = DateTimeConstants.ISO_DATE, timezone = DateTimeConstants.TIME_ZONE)
    LocalDate startDate;

    @JsonFormat(pattern = DateTimeConstants.ISO_DATE, timezone = DateTimeConstants.TIME_ZONE)
    LocalDate endDate;

    @JsonFormat(pattern = DateTimeConstants.ISO_DATETIME_WITH_HOUR_OFFSET, timezone = DateTimeConstants.TIME_ZONE)
    Instant payTime;

    BigDecimal amount;

    Currency currency;

    String contact;

    String contactMobile;

    List chargingWay;

    int lowestAnnualFee;

    int highestAnnualFee;

    String transaction;

    OperationCode operationCode;

    String enterpriseShortName;

    public WmsOpenRequest(WmsOrder order, Integer productId, String paymentRequestIds) {
        orderId = order.getId();
        subscriptionId = order.getSubscriptionId();
        uid = order.getUserId();
        eid = order.getEnterpriseId();
        Contact contactInfo = order.getContact();
        this.productId = productId;
        this.contact = contactInfo.getName();
        contactMobile = contactInfo.getCellphone();
        numberOfYears = (int) YEARS.between(order.getEffectiveFrom(), order.getEffectiveTo());
        startDate = order.getEffectiveFrom();
        endDate = order.getEffectiveTo();
        // TODO - fill this
        payTime = Instant.now();
        // TODO - fill this
        transaction = paymentRequestIds;
        amount = order.getApprovedPrice();
        currency = order.getCurrency();
        chargingWay = order.getChargingRules().stream()
                .map(ChargingWay::new)
                .collect(Collectors.toList());
        lowestAnnualFee = order.getMinPrice();
        highestAnnualFee = order.getMaxPrice();
        enterpriseShortName = order.getEnterpriseShortName();
        operationCode = OperationCode.of(order.getType().getType());
    }

    public enum OperationCode {
        OPEN(1),
        RENEW(2);

        private final int code;

        OperationCode(int code) {
            this.code = code;
        }

        public static OperationCode of(WmsOrderType.Type type) {
            switch (type) {
                case Open:
                    return OPEN;
                case Renew:
                    return RENEW;
                default:
                    throw new IllegalArgumentException("can't create WmsOpenRequest from Recharge");
            }
        }

        @JsonValue
        public int getCode() {
            return code;
        }
    }

    @Data
    public static class ChargingWay {
        Integer greaterThan;

        Integer lessThanOrEqualTo;

        BigDecimal price;

        public ChargingWay(ChargingRule rule) {
            greaterThan = rule.getQuantityFrom();
            lessThanOrEqualTo = rule.getQuantityTo();
            price = rule.getUnitPrice();
        }
    }
}
