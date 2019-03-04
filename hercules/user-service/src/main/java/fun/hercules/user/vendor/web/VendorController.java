package fun.hercules.user.vendor.web;

import fun.hercules.user.vendor.domain.Vendor;
import fun.hercules.user.vendor.service.VendorService;
import fun.hercules.user.vendor.domain.Vendor;
import fun.hercules.user.vendor.service.VendorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/vendors", produces = MediaType.APPLICATION_JSON_VALUE)
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping(path = "/{name}")
    public Vendor getVendor(@PathVariable String name) {
        return vendorService.findVendor(name);
    }
}
