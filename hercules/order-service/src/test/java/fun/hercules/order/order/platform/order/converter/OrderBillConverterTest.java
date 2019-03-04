package fun.hercules.order.order.platform.order.converter;

import fun.hercules.order.order.platform.order.dto.OrderBillDTO;
import fun.hercules.order.order.platform.order.model.OrderBill;
import fun.hercules.order.order.platform.order.model.OrderType;
import fun.hercules.order.order.platform.order.model.PayChannel;
import fun.hercules.order.order.platform.order.model.PayMethod;
import fun.hercules.order.order.platform.user.CurrentUser;
import fun.hercules.order.order.platform.user.Role;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class OrderBillConverterTest {


    @Test
    public void should_convert_order_bill_to_dto_when_user_is_a_platform_admin() {
        OrderBill orderBill = prepareAnOrderBillAfterInit();
        CurrentUser user = Mockito.mock(CurrentUser.class);
        when(user.getRole()).thenReturn(Role.PlatformAdmin.name());

        OrderBillConverter converter = new OrderBillConverter();

        Assertions.assertThat(converter.convert(orderBill))
                .hasFieldOrPropertyWithValue("orderId", orderBill.getOrderId())
                .hasFieldOrPropertyWithValue("productChargeStatus", "待收取")
                .hasFieldOrPropertyWithValue("serviceChargeStatus", "待结算")
                .hasFieldOrPropertyWithValue("commissionChargeStatus", "待扣除");
    }


    private OrderBill prepareAnOrderBillAfterInit() {
        OrderBill bill = OrderBill.builder()
                .id(RandomUtils.nextLong())
                .currency(Currency.getInstance(Locale.CHINA))
                .orderType(OrderType.SKIP_DELETED_CLAUSE)
                .vendor("mvp")
                .payMethod(PayMethod.OFFLINE)
                .payChannel(PayChannel.LOGISTICS)
                .productCharge(BigDecimal.ONE)
                .serviceCharge(BigDecimal.TEN)
                .commissionCharge(BigDecimal.ZERO)
                .build();
        bill.initDefaultStatus();
        return bill;
    }

    @Test
    public void should_convert_order_bill_to_dto_when_channel_is_wms() {

        OrderBill orderBill = prepareAnOrderBill();
        CurrentUser user = Mockito.mock(CurrentUser.class);
        when(user.getRole()).thenReturn(Role.PlatformAdmin.name());

        OrderBillConverter converter = new OrderBillConverter();
        OrderBillDTO dto = converter.convert(orderBill);

        assertThat(dto)
                .hasFieldOrPropertyWithValue("orderId", orderBill.getOrderId())
                .hasFieldOrPropertyWithValue("productChargeStatus", "已收取")
                .hasFieldOrPropertyWithValue("serviceChargeStatus", "无需结算")
                .hasFieldOrPropertyWithValue("commissionChargeStatus", "待收取");
    }

    private OrderBill prepareAnOrderBill() {
        return OrderBill.builder()
                .id(RandomUtils.nextLong())
                .currency(Currency.getInstance(Locale.CHINA))
                .orderType(OrderType.SKIP_DELETED_CLAUSE)
                .vendor("mvp")
                .payMethod(PayMethod.OFFLINE)
                .payChannel(PayChannel.WMS)
                .productCharge(BigDecimal.ONE)
                .serviceCharge(BigDecimal.TEN)
                .commissionCharge(BigDecimal.ZERO)
                .productChargeSettled(true)
                .serviceChargeSettled(true)
                .commissionChargeSettled(false)
                .build();
    }
}