package fun.hercules.user.contact.web;

import fun.hercules.user.contact.domain.Contact;
import fun.hercules.user.contact.service.ContactService;
import fun.hercules.user.contact.domain.Contact;
import fun.hercules.user.contact.service.ContactService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/contacts", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "contacts", description = "Access to contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String createContact(@RequestBody @Valid Contact contact, HttpServletRequest request, HttpServletResponse response) {
        return contactService.createContact(contact);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Contact> readAllContacts(@RequestParam(value = "enterprise_id", required = true) String enterpriseId,
                                         @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return contactService.readContacts(enterpriseId, pageable);
    }

    @RequestMapping(value = "{contactId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Contact readContactsById(@PathVariable String contactId) {
        return contactService.readContactById(contactId);
    }

    @RequestMapping(value = "{contactId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateContactsById(@PathVariable String contactId, @RequestBody @Valid Contact contact) {
        contactService.updateContactById(contactId, contact);
    }

    @RequestMapping(value = "{contactId}/default", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void setDefaultContactsById(@PathVariable String contactId) {
        contactService.setDefaultById(contactId);
    }

    @RequestMapping(value = "{contactId}/deletion", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteContact(@PathVariable String contactId) {
        contactService.deleteContact(contactId);
    }
}
