package fun.hercules.user.contact.web;

import fun.hercules.user.JUnitWebAppTest;
import fun.hercules.user.contact.domain.Contact;
import fun.hercules.user.contact.repository.ContactRepository;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.repository.UserRepository;
import fun.hercules.user.user.security.CurrentUserContext;
import fun.hercules.user.util.TestObjectBuilder;
import fun.hercules.user.user.repository.UserRepository;
import fun.hercules.user.user.security.CurrentUserContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static fun.hercules.user.util.FileUtils.loadTestSuiteResource;
import static fun.hercules.user.utils.AuthorizationUtil.createJwtToken;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Transactional
@RunWith(SpringRunner.class)
@JUnitWebAppTest
public class ContactControllerTest {
    public static final String ENTERPRISE_ID = "qwertyuiop1234567890";
    public static final String OTHER_ENTERPRISE_ID = UUID.randomUUID().toString();
    public static final String CONTACT_NAME = "contact1";
    public static final String CONTACT_NAME2 = "contact2";
    public static final String CONTACT_NAME3 = "contact3";
    public static final String CONTACT_CELLPHONE = "13099998888";
    public static final String OTHER_CONTACT_CELLPHONE = "13099999999";
    public static final String CONTACT_TELEPHONE = "010-88888888";
    public static final Boolean CONTACT_IS_DEFAULT = true;
    public static final Boolean CONTACT_IS_DELETED = true;
    public static final String CONTACT_ID_NOT_EXISTING = "00000";

    private User enterpriseAdmin;
    private User enterpriseUser;
    private User platformAdmin;

    private Contact contact;

    @Autowired
    private MockMvc mvc;
    @Autowired
    private TestObjectBuilder objectBuilder;

    @Autowired
    ContactRepository contactRepository;
    @Autowired
    private UserRepository userRepository;
    @SpyBean
    private CurrentUserContext userContext;

    @Before
    public void setUp() throws Exception {
        User enterpriseUser = objectBuilder.loadUserFromJsonData("enterprise_normal_user.json");
        User enterpriseAdmin = objectBuilder.loadUserFromJsonData("enterprise_admin_user.json");
        User platformAdmin = objectBuilder.loadUserFromJsonData("platform_admin_user.json");

        this.enterpriseUser = userRepository.save(enterpriseUser);
        this.enterpriseAdmin = userRepository.save(enterpriseAdmin);
        this.platformAdmin = userRepository.save(platformAdmin);

        this.enterpriseUser.setEnterpriseId(ENTERPRISE_ID);
        this.enterpriseAdmin.setEnterpriseId(ENTERPRISE_ID);

        when(userContext.getUser()).thenReturn(this.enterpriseUser);

        contact = Contact.builder()
                .name(CONTACT_NAME)
                .cellphone(CONTACT_CELLPHONE)
                .enterpriseId(ENTERPRISE_ID)
                .isDefault(CONTACT_IS_DEFAULT)
                .country("中国")
                .countryAbbr("CHN")
                .province("陕西省")
                .provinceId("61")
                .city("西安市")
                .cityId("01")
                .district("雁塔区")
                .districtId("13")
                .email("test@cn.com")
                .build();
        contactRepository.deleteAll();
    }

    @Test
    public void should_enterprise_user_create_contact() throws Exception {
        MvcResult result = mvc.perform(post("/contacts")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseUser))
                .accept(MediaType.APPLICATION_JSON)
                .content(loadTestSuiteResource("user/contact.json"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        List<Contact> contacts = contactRepository.findAll();
        Contact created = contacts.get(0);
        assertThat(created.getEnterpriseId(), is(ENTERPRISE_ID));
        assertThat(result.getResponse().getContentAsString(), is(created.getId()));
    }

    @Test
    public void should_not_non_enterprise_user_role_create_contact() throws Exception {
        when(userContext.getUser()).thenReturn(this.enterpriseAdmin);

        String json = loadTestSuiteResource("user/contact.json");
        mvc.perform(post("/contacts")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseAdmin))
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        when(userContext.getUser()).thenReturn(this.platformAdmin);

        mvc.perform(post("/contacts")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(platformAdmin))
                .accept(MediaType.APPLICATION_JSON)
                .content(loadTestSuiteResource("user/contact.json"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void should_exist_a_default_contact_for_enterprise() throws Exception {
        mvc.perform(post("/contacts")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseUser))
                .accept(MediaType.APPLICATION_JSON)
                .content(loadTestSuiteResource("user/contact.json"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<Contact> contacts = contactRepository.findAll();
        Contact created = contacts.get(0);
        assertThat(created.isDefault(), is(true));
    }

    @Test
    public void should_not_create_default_contact_when_existing_one() throws Exception {
        contactRepository.save(contact);

        mvc.perform(post("/contacts")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseUser))
                .accept(MediaType.APPLICATION_JSON)
                .content(loadTestSuiteResource("user/contact.json"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<Contact> contacts = contactRepository.findAll();
        Contact created = contacts.get(1);
        assertThat(created.isDefault(), is(not(CONTACT_IS_DEFAULT)));
    }

    @Test
    public void should_create_contact_with_either_cellphone_or_telephone() throws Exception {
        mvc.perform(post("/contacts")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseUser))
                .accept(MediaType.APPLICATION_JSON)
                .content(loadTestSuiteResource("user/contact_without_cellphone.json"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mvc.perform(post("/contacts")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseUser))
                .accept(MediaType.APPLICATION_JSON)
                .content(loadTestSuiteResource("user/contact_without_telephone.json"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mvc.perform(post("/contacts")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseUser))
                .accept(MediaType.APPLICATION_JSON)
                .content(loadTestSuiteResource("user/contact_without_phone.json"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_return_400_when_missing_enterprise_id() throws Exception {
        mvc.perform(post("/contacts")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseUser))
                .accept(MediaType.APPLICATION_JSON)
                .content(loadTestSuiteResource("user/contact_without_enterprise_id.json"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.errors[0].defaultMessage").value("Enterprise id should not be null"));
    }

    @Test
    public void should_return_400_when_missing_params() throws Exception {
        mvc.perform(post("/contacts")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseUser))
                .accept(MediaType.APPLICATION_JSON)
                .content(loadTestSuiteResource("user/contact_without_name.json"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.errors[0].defaultMessage").value("Name should not be null"));
    }

    @Test
    public void should_enterprise_admin_or_user_read_contacts_in_the_same_enterprise() throws Exception {
        contactRepository.save(contact);
        contact.setEnterpriseId(OTHER_ENTERPRISE_ID);
        contactRepository.save(contact);

        when(userContext.getUser()).thenReturn(this.enterpriseUser);
        mvc.perform(get("/contacts?enterprise_id=" + ENTERPRISE_ID)
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(CONTACT_NAME));

        when(userContext.getUser()).thenReturn(this.enterpriseAdmin);
        mvc.perform(get("/contacts?enterprise_id=" + ENTERPRISE_ID)
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(CONTACT_NAME));
    }


    @Test
    public void should_read_contacts_in_desc_order_of_created_time() throws Exception {
        contactRepository.save(contact);
        Thread.sleep(1 * 1000);
        contact.setName(CONTACT_NAME2);
        contactRepository.save(contact);
        Thread.sleep(1 * 1000);
        contact.setName(CONTACT_NAME3);
        contactRepository.save(contact);

        when(userContext.getUser()).thenReturn(this.enterpriseUser);
        mvc.perform(get("/contacts?enterprise_id=" + ENTERPRISE_ID)
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(hasSize(3)))
                .andExpect(jsonPath("$[2].name").value(CONTACT_NAME))
                .andExpect(jsonPath("$[1].name").value(CONTACT_NAME2))
                .andExpect(jsonPath("$[0].name").value(CONTACT_NAME3));
    }

    @Test
    public void should_not_enterprise_admin_or_user_read_contacts_in_other_enterprise() throws Exception {
        contactRepository.save(contact);
        contact.setEnterpriseId(OTHER_ENTERPRISE_ID);
        contactRepository.save(contact);

        when(userContext.getUser()).thenReturn(this.enterpriseUser);
        mvc.perform(get("/contacts?enterprise_id=" + OTHER_ENTERPRISE_ID)
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        when(userContext.getUser()).thenReturn(this.enterpriseAdmin);
        mvc.perform(get("/contacts?enterprise_id=" + OTHER_ENTERPRISE_ID)
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void should_platform_admin_read_contacts_in_all_enterprise() throws Exception {
        contactRepository.save(contact);
        contact.setEnterpriseId(OTHER_ENTERPRISE_ID);
        contactRepository.save(contact);

        when(userContext.getUser()).thenReturn(this.platformAdmin);
        mvc.perform(get("/contacts?enterprise_id=" + ENTERPRISE_ID)
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(platformAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(CONTACT_NAME));

        when(userContext.getUser()).thenReturn(this.platformAdmin);
        mvc.perform(get("/contacts?enterprise_id=" + OTHER_ENTERPRISE_ID)
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(platformAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(CONTACT_NAME));
    }

    @Test
    public void should_read_empty_list_when_no_contacts_in_enterprise() throws Exception {
        mvc.perform(get("/contacts?enterprise_id=" + ENTERPRISE_ID)
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(hasSize(0)));
    }

    @Test
    public void should_not_read_deleted_contact() throws Exception {
        contact.setDeleted(CONTACT_IS_DELETED);
        contactRepository.save(contact);

        mvc.perform(get("/contacts?enterprise_id=" + ENTERPRISE_ID)
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(hasSize(0)));
    }

    @Test
    public void should_enterprise_admin_or_user_read_contact_by_id_in_the_same_enterprise() throws Exception {
        final Contact created = contactRepository.save(contact);

        when(userContext.getUser()).thenReturn(this.enterpriseUser);
        mvc.perform(get("/contacts/" + created.getId())
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(created.getName()));

        when(userContext.getUser()).thenReturn(this.enterpriseAdmin);
        mvc.perform(get("/contacts/" + created.getId())
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(created.getName()));
    }

    @Test
    public void should_not_enterprise_admin_or_user_read_contact_by_id_in_other_enterprise() throws Exception {
        contact.setEnterpriseId(OTHER_ENTERPRISE_ID);
        final Contact created = contactRepository.save(contact);

        when(userContext.getUser()).thenReturn(this.enterpriseUser);
        mvc.perform(get("/contacts/" + created.getId())
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        when(userContext.getUser()).thenReturn(this.enterpriseAdmin);
        mvc.perform(get("/contacts/" + created.getId())
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void should_platform_admin_read_contact_by_id_() throws Exception {
        final Contact created = contactRepository.save(contact);
        contact.setEnterpriseId(OTHER_ENTERPRISE_ID);
        final Contact createdOther = contactRepository.save(contact);

        when(userContext.getUser()).thenReturn(this.platformAdmin);

        mvc.perform(get("/contacts/" + created.getId())
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(platformAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(created.getName()));

        mvc.perform(get("/contacts/" + createdOther.getId())
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(platformAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(createdOther.getName()));
    }

    @Test
    public void should_return_not_found_when_contact_not_exists() throws Exception {
        mvc.perform(get("/contacts/" + CONTACT_ID_NOT_EXISTING)
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_enterprise_user_update_contact_in_the_same_enterprise() throws Exception {
        final Contact created = contactRepository.save(contact);

        mvc.perform(post("/contacts/" + created.getId())
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadTestSuiteResource("user/newContact.json")))
                .andExpect(status().isAccepted());

        List<Contact> contacts = contactRepository.findAll();
        assertThat(contacts.get(0).getName(), is("小明"));
        assertThat(contacts.get(0).getCountry(), is("中国大陆"));
        assertThat(contacts.get(0).getPostcode(), is("716000"));
        assertThat(contacts.get(0).getEmail(), is("test2@us.com"));
    }

    @Test
    public void should_not_enterprise_user_update_contact_in_other_enterprise() throws Exception {
        contact.setEnterpriseId(OTHER_ENTERPRISE_ID);
        final Contact created = contactRepository.save(contact);

        mvc.perform(post("/contacts/" + created.getId())
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadTestSuiteResource("user/newContact.json")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void should_not_non_enterprise_user_update_contact() throws Exception {
        final Contact created = contactRepository.save(contact);

        when(userContext.getUser()).thenReturn(this.enterpriseAdmin);
        mvc.perform(post("/contacts/" + created.getId())
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseAdmin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadTestSuiteResource("user/newContact.json")))
                .andExpect(status().isForbidden());

        when(userContext.getUser()).thenReturn(this.platformAdmin);
        mvc.perform(post("/contacts/" + created.getId())
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(platformAdmin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadTestSuiteResource("user/newContact.json")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void should_update_contact_with_either_cell_phone_or_telephone() throws Exception {
        final Contact created = contactRepository.save(contact);

        mvc.perform(post("/contacts/" + created.getId())
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadTestSuiteResource("user/newContact_without_phone.json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_update_return_not_found_when_contact_not_found() throws Exception {
        mvc.perform(post("/contacts/" + CONTACT_ID_NOT_EXISTING)
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadTestSuiteResource("user/newContact.json")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_enterprise_user_delete_contact_in_the_same_enterprise() throws Exception {
        final Contact created = contactRepository.save(contact);

        mvc.perform(post("/contacts/" + created.getId() + "/deletion")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        List<Contact> contacts = contactRepository.findByDeleted(true);
        assertThat(contacts.size(), is(1));
    }

    @Test
    public void should_not_enterprise_user_delete_contact_in_other_enterprise() throws Exception {
        contact.setEnterpriseId(OTHER_ENTERPRISE_ID);
        final Contact created = contactRepository.save(contact);

        mvc.perform(post("/contacts/" + created.getId() + "/deletion")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        List<Contact> contacts = contactRepository.findByDeleted(false);
        assertThat(contacts.size(), is(1));
    }

    @Test
    public void should_not_non_enterprise_user_delete_contact() throws Exception {
        final Contact created = contactRepository.save(contact);

        when(userContext.getUser()).thenReturn(this.enterpriseAdmin);
        mvc.perform(post("/contacts/" + created.getId() + "/deletion")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        when(userContext.getUser()).thenReturn(this.platformAdmin);
        mvc.perform(post("/contacts/" + created.getId() + "/deletion")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(platformAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void should_enterprise_user_set_default_contact() throws Exception {
        contact.setDefault(CONTACT_IS_DEFAULT);
        final Contact defaultContact = contactRepository.save(contact);

        contact.setDefault(!CONTACT_IS_DEFAULT);
        final Contact normalContact = contactRepository.save(contact);

        mvc.perform(post("/contacts/" + normalContact.getId() + "/default")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        assertThat(contactRepository.findOne(defaultContact.getId()).isDefault(), is(false));
        assertThat(contactRepository.findOne(normalContact.getId()).isDefault(), is(true));
    }

    @Test
    public void should_not_non_enterprise_user_set_default_contact() throws Exception {
        contact.setDefault(CONTACT_IS_DEFAULT);
        contactRepository.save(contact);

        contact.setDefault(!CONTACT_IS_DEFAULT);
        final Contact normalContact = contactRepository.save(contact);

        when(userContext.getUser()).thenReturn(this.platformAdmin);
        mvc.perform(post("/contacts/" + normalContact.getId() + "/default")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(platformAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        when(userContext.getUser()).thenReturn(this.enterpriseAdmin);
        mvc.perform(post("/contacts/" + normalContact.getId() + "/default")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}