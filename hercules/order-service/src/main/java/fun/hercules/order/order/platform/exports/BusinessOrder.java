package fun.hercules.order.order.platform.exports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fun.hercules.order.order.platform.order.model.CancelReason;
import fun.hercules.order.order.platform.order.model.OrderStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

public interface BusinessOrder extends Auditable {
    String getId();

    String getUserId();

    void setUserId(String userId);

    String getUserRole();

    void setUserRole(String userRole);

    String getEnterpriseId();

    void setEnterpriseId(String enterpriseId);

    OrderStatus getStatus();

    void setStatus(OrderStatus status);

    CancelReason getCancelReason();

    void setCancelReason(CancelReason cancelReason);

    @JsonIgnore
    default BusinessType getBusinessType() {
        return this.getClass().getDeclaredAnnotation(BusinessType.class);
    }

    List<String> getOrderSubTypesForDisplay();

    default String getOrderType() {
        return getBusinessType().value();
    }

    String getVendor();

    default List<String> getOrderSubTypes() {
        return Arrays.asList(getOrderType());
    }

    default BusinessOrder markDeleted() {
        setDeleted(true);
        return this;
    }

    void setRefundStatus(Boolean status);

    Boolean getRefundStatus();

    BigDecimal getTotalPrice();

    Currency getCurrency();
}
