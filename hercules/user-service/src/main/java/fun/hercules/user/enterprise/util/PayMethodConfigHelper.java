package fun.hercules.user.enterprise.util;

import fun.hercules.user.enterprise.domain.Enterprise;
import fun.hercules.user.enterprise.domain.PayMethod;
import fun.hercules.user.vendor.domain.Vendor;
import fun.hercules.user.vendor.repository.VendorRepository;
import fun.hercules.user.enterprise.domain.Enterprise;
import fun.hercules.user.enterprise.domain.PayMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 支付方式配置助手类
 */
@Service
public class PayMethodConfigHelper {

    private final VendorRepository vendorRepository;

    public PayMethodConfigHelper(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    // 查询企业默认支付方式
    public List<PayMethod> getDefaultPayMethods(Enterprise enterprise) {
        List<PayMethod> payMethods = new ArrayList<>();
        List<Vendor> vendors = vendorRepository.findAll();
        vendors.stream().forEach(vendor -> {
                    PayMethod payMethod = PayMethod.builder()
                            .enterprise(enterprise)
                            .orderType(vendor.getBusinessType().getValue())
                            .methods(PayMethod.Type.OFFLINE.name())
                            .build();
                    payMethods.add(payMethod);
                }
        );
        return payMethods;
    }
}
