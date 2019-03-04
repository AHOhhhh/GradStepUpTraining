package fun.hercules.order.order.clients;

import fun.hercules.order.order.clients.dto.Enterprise;
import fun.hercules.order.order.clients.dto.User;
import fun.hercules.order.order.clients.fallback.UserClientFallBack;
import fun.hercules.order.order.common.dto.Contact;
import fun.hercules.order.order.common.dto.OrderNotification;
import fun.hercules.order.order.common.dto.Vendor;
import fun.hercules.order.order.common.signature.FeignConfiguration;
import fun.hercules.order.order.common.signature.annotations.JwtSignature;
import fun.hercules.order.order.platform.order.model.PayMethodConfigDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@JwtSignature
@FeignClient(value = "user-service", fallback = UserClientFallBack.class)
public interface UserServiceClient {

    @RequestMapping(method = RequestMethod.GET, path = "/users/{id}")
    User getUserById(@PathVariable("id") String id);

    @RequestMapping(method = RequestMethod.GET, path = "/enterprises/{id}")
    Enterprise getEnterpriseById(@PathVariable("id") String id);

    @RequestMapping(value = "/enterprises/{enterpriseId}/contacts", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    List<Contact> getContacts(@PathVariable("enterpriseId") String enterpriseId);

    @RequestMapping(method = RequestMethod.GET, path = "/enterprises/{id}/pay-methods")
    List<PayMethodConfigDTO> getEnterprisePayMethods(@PathVariable("id") String id);

    @PostMapping(path = "/notifications")
    void createOrderNotification(@RequestBody OrderNotification orderNotification);

    @GetMapping(path = "/vendors/{name}")
    Vendor getVendor(@PathVariable("name") String name);
}
