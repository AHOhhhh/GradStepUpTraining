package fun.hercules.order.order.platform.order.service;

import fun.hercules.order.order.JUnitWebAppTest;
import fun.hercules.order.order.OrderIntegrationTestBase;
import fun.hercules.order.order.platform.order.dto.UploadExcelResponse;
import fun.hercules.order.order.platform.order.model.OrderBill;
import fun.hercules.order.order.platform.order.model.PayChannel;
import fun.hercules.order.order.platform.order.validators.OrderBillImportationValidator;
import fun.hercules.order.order.platform.user.Role;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JUnitWebAppTest
public class OrderBillExcelServiceTest extends OrderIntegrationTestBase {

    @Autowired
    private OrderBillExcelService orderBillExcelService;

    @Autowired
    private OrderBillImportationValidator orderBillImportationValidator;

    @Before
    public void setUp() {
        switchRole(Role.PlatformAdmin);
    }

    @Test
    public void shouldValidateDuplicatedOrders() {
        List bodyList = Arrays.asList(
                generateItem("1"),
                generateItem("2"),
                generateItem("2"),
                generateItem("3"),
                generateItem("3"),
                generateItem("3")
        );

        UploadExcelResponse uploadExcelResponse = UploadExcelResponse.builder().build();

        orderBillExcelService.validate(bodyList, Arrays.asList(), uploadExcelResponse);

        Assert.assertEquals("failed", uploadExcelResponse.getStatus());
        List duplicatedOrderIds = uploadExcelResponse.getDuplicatedOrderIds();
        Assert.assertEquals(2, duplicatedOrderIds.size());
        Assert.assertEquals("2", duplicatedOrderIds.get(0));
        Assert.assertEquals("3", duplicatedOrderIds.get(1));
    }

    @Test
    public void shouldValidateNonExistOrders() {
        List bodyList = Arrays.asList(
                generateItem("1"),
                generateItem("2"),
                generateItem("3"),
                generateItem("4")
        );
        List<OrderBill> orderBills = Arrays.asList(
                prepareAnOrderBill("1"),
                prepareAnOrderBill("2")
        );

        UploadExcelResponse result = UploadExcelResponse.builder().build();

        orderBillExcelService.validate(bodyList, orderBills, result);

        Assert.assertEquals("failed", result.getStatus());
        List nonExistOrderIds = result.getNonExistOrderIds();
        Assert.assertEquals(2, nonExistOrderIds.size());
        Assert.assertEquals("3", nonExistOrderIds.get(0));
        Assert.assertEquals("4", nonExistOrderIds.get(1));
    }

    @Test
    public void shouldValidateDuplicatedAndNonExistOrderIdsAndErrorStatus() {
        List bodyList = Arrays.asList(
                generateItem("1"),
                generateItem("2"),
                generateItem("3"),
                generateItem("3"),
                generateItem("4"),
                generateItem("4"),
                generateErrorStatusItem("5")
        );
        List<OrderBill> orderBills = Arrays.asList(
                prepareAnOrderBill("1"),
                prepareAnOrderBill("2"),
                prepareAnOrderBill("3"),
                prepareAnOrderBill("5")
        );

        UploadExcelResponse result = UploadExcelResponse.builder().build();

        orderBillExcelService.validate(bodyList, orderBills, result);

        Assert.assertEquals("failed", result.getStatus());
        List duplicatedOrderIds = result.getDuplicatedOrderIds();
        Assert.assertEquals(2, duplicatedOrderIds.size());
        Assert.assertEquals("3", duplicatedOrderIds.get(0));
        Assert.assertEquals("4", duplicatedOrderIds.get(1));
        List nonExistOrderIds = result.getNonExistOrderIds();
        Assert.assertEquals(2, nonExistOrderIds.size());
        Assert.assertEquals("4", nonExistOrderIds.get(0));
        List errorStatusOrderIds = result.getStatusErrorOrderIds();
        assertThat(result.getStatusErrorOrderIds()).containsExactly("5");
        Assert.assertEquals(1, errorStatusOrderIds.size());
        Assert.assertEquals("5", errorStatusOrderIds.get(0));
    }

    @Test
    public void shouldPassValidationIfAllDataIsValid() {
        List bodyList = Arrays.asList(
                generateItem("1"),
                generateItem("2"),
                generateItem("3"),
                generateItem("4")
        );
        List<OrderBill> orderBills = Arrays.asList(
                prepareAnOrderBill("1"),
                prepareAnOrderBill("2"),
                prepareAnOrderBill("3"),
                prepareAnOrderBill("4")
        );

        UploadExcelResponse result = UploadExcelResponse.builder().build();

        orderBillExcelService.validate(bodyList, orderBills, result);
        Assert.assertTrue(result.isSucceed());
    }

    private OrderBill prepareAnOrderBill(String orderId) {
        return OrderBill.builder()
                .orderId(orderId)
                .payChannel(PayChannel.LOGISTICS)
                .build();
    }

    private Map generateErrorStatusItem(String orderId) {
        return new HashMap<String, Object>() {
            {
                put("productCharge", 0.05);
                put("orderType", "航空货运");
                put("createdAt", "Tue Jan 09 07:50:46 CST 2018");
                put("serviceCharge", "0.05");
                put("orderId", orderId);
                put("payMethod", "线下");
                put("vendor", "大力神货运");
                put("commissionCharge", "0.0");
                put("payChannel", "平台");
                put("productChargeStatus", "");
                put("serviceChargeStatus", "已结算");
                put("commissionChargeStatus", "已扣除");
            }
        };
    }


    private Map generateItem(String orderId) {
        return new HashMap<String, Object>() {
            {
                put("productCharge", 0.05);
                put("orderType", "航空货运");
                put("createdAt", "Tue Jan 09 07:50:46 CST 2018");
                put("serviceCharge", "0.05");
                put("productChargeStatus", "已收取");
                put("serviceChargeStatus", "已结算");
                put("commissionChargeStatus", "已扣除");
                put("orderId", orderId);
                put("payMethod", "线下");
                put("vendor", "大力神货运");
                put("commissionCharge", "0.0");
                put("payChannel", "平台");
            }
        };
    }
}