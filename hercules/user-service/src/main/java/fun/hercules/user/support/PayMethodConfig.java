package fun.hercules.user.support;

import fun.hercules.user.enterprise.domain.PayMethod;
import fun.hercules.user.vendor.domain.Vendor;
import fun.hercules.user.vendor.service.VendorService;
import fun.hercules.user.enterprise.domain.PayMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 支付方式配置
 */
@Service
public class PayMethodConfig {

    private final VendorService vendorService;

    public PayMethodConfig(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    public List<PayMethodConfigItem> getItems() {
        ArrayList<PayMethodConfigItem> items = new ArrayList<>();
        List<Vendor> vendors = vendorService.findAllVendors();
        vendors.stream().forEach(vendor -> {
            PayMethodConfigItem item = PayMethodConfigItem.builder()
                    .orderType(vendor.getBusinessType().getValue())
                    .editable(Arrays.asList(vendor.getPayMethods().split(",")))
                    .defaults(Arrays.asList(PayMethod.Type.ONLINE.name()))
                    .build();
            items.add(item);
        });
        return items;
    }
}
