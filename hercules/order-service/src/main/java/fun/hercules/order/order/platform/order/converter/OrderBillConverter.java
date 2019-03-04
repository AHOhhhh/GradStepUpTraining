package fun.hercules.order.order.platform.order.converter;

import com.google.common.base.Converter;
import fun.hercules.order.order.platform.order.dto.OrderBillDTO;
import fun.hercules.order.order.platform.order.model.OrderBill;
import fun.hercules.order.order.platform.order.model.PayChannel;
import fun.hercules.order.order.platform.order.model.PayMethod;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static fun.hercules.order.order.platform.order.utils.ChargeStatusMapping.getCommissionChargeStatus;
import static fun.hercules.order.order.platform.order.utils.ChargeStatusMapping.getProductChargeStatus;
import static fun.hercules.order.order.platform.order.utils.ChargeStatusMapping.getServiceChargeStatus;

@Component
@EqualsAndHashCode(callSuper = false)
public class OrderBillConverter extends Converter<OrderBill, OrderBillDTO> {

    @Autowired
    public OrderBillConverter() {
    }

    @Override
    protected OrderBillDTO doForward(@NonNull OrderBill orderBill) {
        return OrderBillDTO.builder()
                .orderId(orderBill.getOrderId())
                .orderType(Optional.ofNullable(OrderBill.orderTypeMap.get(orderBill.getOrderType())).orElse(orderBill.getOrderType()))
                .vendor(Optional.ofNullable(OrderBill.vendorMap.get(orderBill.getVendor())).orElse(orderBill.getVendor()))
                .payMethod(Optional.ofNullable(orderBill.getPayMethod()).map(PayMethod::getValue).orElse(null))
                .payChannel(Optional.ofNullable(orderBill.getPayChannel()).map(PayChannel::getValue).orElse(null))
                .productCharge(orderBill.getProductCharge())
                .serviceCharge(orderBill.getServiceCharge())
                .commissionCharge(orderBill.getCommissionCharge())
                .createdAt(orderBill.getCreatedAt())
                .currency(orderBill.getCurrency().getCurrencyCode())
                .productChargeStatus(getProductChargeStatus(orderBill.isProductChargeSettled()))
                .serviceChargeStatus(getServiceChargeStatus(orderBill.isServiceChargeSettled(), orderBill.getPayChannel()))
                .commissionChargeStatus(getCommissionChargeStatus(orderBill.isCommissionChargeSettled(), orderBill.getPayChannel()))
                .build();
    }

    @Override
    protected OrderBill doBackward(@NonNull OrderBillDTO orderBillDTO) {
        throw new UnsupportedOperationException("unsupported operation.");
    }

}
