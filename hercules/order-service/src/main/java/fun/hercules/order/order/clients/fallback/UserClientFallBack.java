package fun.hercules.order.order.clients.fallback;

import fun.hercules.order.order.clients.UserServiceClient;
import fun.hercules.order.order.clients.dto.Enterprise;
import fun.hercules.order.order.clients.dto.User;
import fun.hercules.order.order.common.dto.Contact;
import fun.hercules.order.order.common.dto.OrderNotification;
import fun.hercules.order.order.common.dto.Vendor;
import fun.hercules.order.order.platform.order.model.PayMethodConfigDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserClientFallBack implements UserServiceClient {
    @Override
    public User getUserById(String id) {
        User mockUser = new User();
        mockUser.setUsername("user service unused");
        return mockUser;
    }

    @Override
    public Enterprise getEnterpriseById(String id) {
        Enterprise mockEnterprise = new Enterprise();
        mockEnterprise.setName("user service unused");
        return mockEnterprise;
    }

    @Override
    public List<Contact> getContacts(String enterpriseId) {
        Contact mockContact = new Contact();
        mockContact.setName("user service unused");
        List<Contact> mockContacts = new ArrayList<>();
        mockContacts.add(mockContact);
        return mockContacts;
    }

    @Override
    public List<PayMethodConfigDTO> getEnterprisePayMethods(String id) {
        return new ArrayList<>();
    }

    @Override
    public void createOrderNotification(OrderNotification orderNotification) {

    }

    @Override
    public Vendor getVendor(String name) {
        Vendor mockVendor = new Vendor();
        mockVendor.setName("user service unused");
        return mockVendor;
    }
}
