package fun.hercules.user.vendor.service;

import com.fasterxml.jackson.core.type.TypeReference;
import fun.hercules.user.utils.JsonUtils;
import fun.hercules.user.vendor.domain.Vendor;
import fun.hercules.user.vendor.repository.VendorRepository;
import fun.hercules.user.vendor.repository.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class VendorService {
    private final VendorRepository vendorRepository;

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }


    // 初始启动时，预创建供应商信息，数据从/fixtures/vendor.json中获得
    @PostConstruct
    public void createPredefinedVendors() throws IOException {
        List<Vendor> vendors = JsonUtils.unmarshal(new ClassPathResource("/fixtures/vendor.json").getInputStream(),
                new TypeReference<List<Vendor>>() {
                });

        vendors.forEach(vendor -> {
            if (!vendorRepository.exists(vendor.getId())) {
                log.info("create/update vendor {}", vendor);
                try {
                    vendorRepository.save(vendor);
                } catch (RuntimeException e) {
                    log.warn(String.format("failed to save vendor %s", vendor), e);
                }
            }
        });
    }

    public List<Vendor> findAllVendors() {
        return vendorRepository.findAll();
    }

    public Vendor findVendor(String vendorName) {
        return vendorRepository.findOne(vendorName);
    }
}
