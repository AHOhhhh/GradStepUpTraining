package fun.hercules.user.user.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import fun.hercules.user.JUnitWebAppTest;
import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.enterprise.domain.Enterprise;
import fun.hercules.user.enterprise.domain.EnterpriseBase;
import fun.hercules.user.enterprise.repository.EnterpriseRepository;
import fun.hercules.user.operation.domain.OperationLog;
import fun.hercules.user.operation.domain.OperationType;
import fun.hercules.user.operation.repository.OperationLogRepository;
import fun.hercules.user.user.domain.Role;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.repository.UserRepository;
import fun.hercules.user.user.service.RoleService;
import fun.hercules.user.util.TestObjectBuilder;
import fun.hercules.user.utils.captcha.CaptchaService;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import fun.hercules.user.user.domain.Role;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.repository.UserRepository;
import fun.hercules.user.user.service.RoleService;
import fun.hercules.user.util.FileUtils;
import fun.hercules.user.util.TestObjectBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static fun.hercules.user.common.constants.Status.DISABLED;
import static fun.hercules.user.util.FileUtils.loadTestSuiteResource;
import static fun.hercules.user.utils.AuthorizationUtil.createJwtToken;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Transactional
@RunWith(SpringRunner.class)
@JUnitWebAppTest
@SuppressFBWarnings(value = "DLS_DEAD_LOCAL_STORE")
public class UserControllerTest {
    private final String enterpriseAdminRegistrationContent = FileUtils.loadTestSuiteResource("user/create_enterprise_admin_user_request.json");
    private final String enterpriseAdminWithWrongRoleRegistrationContent =
            FileUtils.loadTestSuiteResource("user/enterprise_admin_user_with_wrong_role.json");
    private final String normalUserRegistrationContent = FileUtils.loadTestSuiteResource("user/create_enterprise_normal_user_request.json");

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestObjectBuilder userBuilder;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private OperationLogRepository operationLogRepository;

    private User adminUser;

    private User enterpriseUser;

    private User platformAdmin;

    private Enterprise enterprise;

    @InjectMocks
    private TestObjectBuilder testObjectBuilder;

    @MockBean
    private CaptchaService captchaService;

    @Before
    public void setUp() throws Exception {
        adminUser = userBuilder.loadUserFromJsonData("enterprise_admin_user.json");
        enterpriseUser = userBuilder.loadUserFromJsonData("enterprise_normal_user.json");
        platformAdmin = userBuilder.loadUserFromJsonData("platform_admin_user.json");

        enterprise = testObjectBuilder.loadEnterpriseFromJson("enterprise_qualification.json");
    }

    @Test
    public void shouldReturnUserInformationWithoutPassword() throws Exception {
        User user = userRepository.save(adminUser);
        mvc.perform(get("/enterprise-admin/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value("testadmin"))
                .andExpect(jsonPath("$.fullname").value("Administrator"))
                .andExpect(jsonPath("$.cellphone").value("1234567"))
                .andExpect(jsonPath("$.telephone").value("7654321"))
                .andExpect(jsonPath("$.email").value("admin@thoughtworks.com"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.role").value(Role.Type.EnterpriseAdmin.toString()));
    }

    @Test
    public void shouldCreateEnterpriseAdminWithHashedPassword() throws Exception {
        MvcResult result = mvc.perform(post("/enterprise-admin")
                .content(enterpriseAdminRegistrationContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, startsWith("Bearer")))
                .andReturn();

        String userId = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id").toString();
        Optional<User> user = userRepository.findByUsername("testadmin");
        assertTrue(user.isPresent());
        assertThat(user.get().getId(), is(userId));
        assertTrue(encoder.matches("letmein123!", user.get().getPassword()));
    }

    @Test
    public void shouldCreateEnterpriseAdminFailedWhenTheRequestRoleIsWrong() throws Exception {
        mvc.perform(post("/enterprise-admin")
                .content(enterpriseAdminWithWrongRoleRegistrationContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldCanCreateNormalUser() throws Exception {
        this.enterprise.setValidationStatus(EnterpriseBase.ValidationStatus.Authorized);
        Enterprise enterprise = enterpriseRepository.save(this.enterprise);
        adminUser.setEnterpriseId(enterprise.getId());
        User savedUser = userRepository.save(this.adminUser);
        MvcResult result = mvc.perform(post("/enterprise-user")
                .header("Authorization", createJwtToken(savedUser))
                .content(normalUserRegistrationContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();

        String userId = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id").toString();
        Optional<User> addedUser = userRepository.findByUsername("testUser");
        assertTrue(addedUser.isPresent());
        assertThat(addedUser.get().getId(), is(userId));

        List<OperationLog> operationLogs = operationLogRepository.findAll();
        assertThat(operationLogs.size(), is(1));
        assertThat(operationLogs.get(0).getEnterpriseId(), is(enterprise.getId()));
        assertThat(operationLogs.get(0).getEnterpriseName(), is(enterprise.getName()));
        assertThat(operationLogs.get(0).getTargetUserId(), is(addedUser.get().getId()));
        assertThat(operationLogs.get(0).getTargetUserName(), is(addedUser.get().getUsername()));
        assertThat(operationLogs.get(0).getOperatorRole(), is("EnterpriseAdmin"));
        assertThat(operationLogs.get(0).getType(), is(OperationType.CreateEnterpriseUser));
    }

    @Test
    public void shouldCreateNormalUserFailedWithNoAuthorization() throws Exception {
        this.enterprise.setValidationStatus(EnterpriseBase.ValidationStatus.Authorized);
        Enterprise enterprise = enterpriseRepository.save(this.enterprise);

        Role userRole = roleService.getByType(Role.Type.EnterpriseUser);
        adminUser.setEnterpriseId("111111");
        adminUser.setRole(userRole);
        adminUser.setEnterpriseId(enterprise.getId());
        User savedUser = userRepository.save(this.adminUser);
        mvc.perform(post("/enterprise-user")
                .header("Authorization", createJwtToken(savedUser))
                .content(normalUserRegistrationContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    public void shouldCanChangeNormalUserStatus() throws Exception {
        Enterprise enterprise = enterpriseRepository.save(this.enterprise);
        adminUser.setEnterpriseId(enterprise.getId());
        User savedUser = userRepository.save(this.adminUser);
        this.enterpriseUser.setEnterpriseId(enterprise.getId());
        User savedNormalUser = userRepository.save(this.enterpriseUser);

        String url = "/enterprise-user/" + savedNormalUser.getId() + "/status";
        mvc.perform(post(url)
                .header("Authorization", createJwtToken(savedUser))
                .content("{\"status\" : \"DISABLED\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();

        User updatedUser = userRepository.findOne(savedNormalUser.getId());
        assertNotNull(updatedUser);
        assertThat(updatedUser.getStatus(), is(DISABLED));

        List<OperationLog> operationLogs = operationLogRepository.findAll();
        assertThat(operationLogs.size(), is(1));
        assertThat(operationLogs.get(0).getEnterpriseId(), is(enterprise.getId()));
        assertThat(operationLogs.get(0).getEnterpriseName(), is(enterprise.getName()));
        assertThat(operationLogs.get(0).getTargetUserId(), is(updatedUser.getId()));
        assertThat(operationLogs.get(0).getTargetUserName(), is(updatedUser.getUsername()));
        assertThat(operationLogs.get(0).getOperatorRole(), is("EnterpriseAdmin"));
        assertThat(operationLogs.get(0).getType(), is(OperationType.DisableEnterpriseUser));
    }

    @Test
    public void shouldRejectUserRegistrationWhenPasswordIsToShort() throws Exception {
        DocumentContext content = JsonPath.parse(enterpriseAdminRegistrationContent)
                .set("$.password", "1etme!n");
        mvc.perform(post("/enterprise-admin")
                .content(content.jsonString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.PASSWORD_LENGTH_TOO_SHORT.getCode()));
    }

    @Test
    public void shouldRejectUserRegistrationWhenPasswordIsToWeak() throws Exception {
        DocumentContext content = JsonPath.parse(enterpriseAdminRegistrationContent)
                .set("$.password", "LetMeInPlease");
        mvc.perform(post("/enterprise-admin")
                .content(content.jsonString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.WEAK_PASSWORD.getCode()));

    }


    @Test
    public void shouldRejectUserRegistrationWhenUserNameConflicts() throws Exception {
        mvc.perform(post("/enterprise-admin")
                .content(enterpriseAdminRegistrationContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mvc.perform(post("/enterprise-admin")
                .content(enterpriseAdminRegistrationContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldRejectUserRegistrationWhenBothCellphoneAndTelephoneNumberIsMissing() throws Exception {
        DocumentContext content = JsonPath.parse(enterpriseAdminRegistrationContent)
                .delete("$.cellphone")
                .delete("$.telephone");
        mvc.perform(post("/enterprise-admin")
                .content(content.jsonString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.MISSING_PHONE_NUMBER.getCode()));
    }

    @Test
    public void shouldUpdateEnterpriseAdminUserInformation() throws Exception {
        Enterprise enterprise = enterpriseRepository.save(this.enterprise);
        adminUser.setEnterpriseId(enterprise.getId());
        adminUser = userRepository.save(this.adminUser);
        String id = adminUser.getId();
        User updatedUser = userRepository.findByUsername("testadmin").get();
        String newEmailAddress = "aaaaaaa@gmail.com";
        updatedUser.setEmail(newEmailAddress);
        MvcResult result = mvc.perform(post("/enterprise-admin/" + id)
                .header("Authorization", createJwtToken(adminUser))
                .content(objectMapper.writeValueAsString(updatedUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();
        String userId = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id").toString();
        updatedUser = userRepository.getOne(userId);
        assertEquals(updatedUser.getEmail(), newEmailAddress);

        List<OperationLog> operationLogs = operationLogRepository.findAll();
        assertThat(operationLogs.size(), is(1));
        assertThat(operationLogs.get(0).getEnterpriseId(), is(enterprise.getId()));
        assertThat(operationLogs.get(0).getEnterpriseName(), is(enterprise.getName()));
        assertThat(operationLogs.get(0).getTargetUserId(), is(updatedUser.getId()));
        assertThat(operationLogs.get(0).getTargetUserName(), is(updatedUser.getUsername()));
        assertThat(operationLogs.get(0).getOperatorRole(), is("EnterpriseAdmin"));
        assertThat(operationLogs.get(0).getType(), is(OperationType.UpdateEnterpriseAdminInfo));
    }

    @Test
    public void shouldRejectUpdateEnterpriseAdminUserInformationWhenFullnameIsMissing()
            throws Exception {
        adminUser = userRepository.save(this.adminUser);
        String id = adminUser.getId();
        Optional<User> originalUser = userRepository.findByUsername("testadmin");
        User updatedUser = originalUser.get();
        updatedUser.setFullname(null);
        mvc.perform(post("/enterprise-admin/" + id)
                .content(objectMapper.writeValueAsString(updatedUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.MISSING_FULL_NAME.getCode()));
    }

    @Test
    public void shouldRejectUpdateEnterpriseAdminUserInformationWhenBothCellphoneAndTelephoneNumberIsMissing()
            throws Exception {
        userRepository.save(this.adminUser);
        String id = adminUser.getId();
        Optional<User> updatedUser = userRepository.findByUsername("testadmin");
        User addedUser = updatedUser.get();
        addedUser.setTelephone("");
        addedUser.setCellphone("");
        mvc.perform(post("/enterprise-admin/" + id)
                .content(objectMapper.writeValueAsString(addedUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.MISSING_PHONE_NUMBER.getCode()));
    }

    @Test
    public void shouldUpdateEnterpriseUserInformation() throws Exception {
        Enterprise enterprise = enterpriseRepository.save(this.enterprise);
        adminUser.setEnterpriseId(enterprise.getId());
        adminUser = userRepository.save(this.adminUser);
        enterpriseUser.setEnterpriseId(enterprise.getId());
        enterpriseUser = userRepository.save(this.enterpriseUser);
        String userId = enterpriseUser.getId();

        User addedUser = userRepository.getOne(userId);
        addedUser.setEmail("adminUser@enterprise.com");
        addedUser.setTelephone("12345678");
        addedUser.setCellphone("11111111");
        addedUser.setFullname("enterpriseFullName");

        mvc.perform(post("/enterprise-user/" + userId)
                .header("Authorization", createJwtToken(adminUser))
                .content(objectMapper.writeValueAsString(addedUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();

        User updatedUser = userRepository.getOne(userId);
        assertThat(updatedUser.getEmail(), is("adminUser@enterprise.com"));
        assertThat(updatedUser.getCellphone(), is("11111111"));
        assertThat(updatedUser.getTelephone(), is("12345678"));
        assertThat(updatedUser.getFullname(), is("enterpriseFullName"));

        List<OperationLog> operationLogs = operationLogRepository.findAll();
        assertThat(operationLogs.size(), is(1));
        assertThat(operationLogs.get(0).getEnterpriseId(), is(enterprise.getId()));
        assertThat(operationLogs.get(0).getEnterpriseName(), is(enterprise.getName()));
        assertThat(operationLogs.get(0).getTargetUserId(), is(updatedUser.getId()));
        assertThat(operationLogs.get(0).getTargetUserName(), is(updatedUser.getUsername()));
        assertThat(operationLogs.get(0).getOperatorRole(), is("EnterpriseAdmin"));
        assertThat(operationLogs.get(0).getType(), is(OperationType.UpdateEnterpriseUser));
    }

    @Test
    public void shouldUpdateEnterpriseUserFailedWithNoAuthorization() throws Exception {
        adminUser.setEnterpriseId("22222");
        adminUser = userRepository.save(this.adminUser);
        enterpriseUser.setEnterpriseId("111111");
        enterpriseUser = userRepository.save(this.enterpriseUser);

        enterpriseUser.setEmail("adminUser@enterprise.com");
        enterpriseUser.setTelephone("12345678");
        enterpriseUser.setCellphone("11111111");
        enterpriseUser.setFullname("enterpriseFullName");

        mvc.perform(post("/enterprise-user/" + enterpriseUser.getId())
                .header("Authorization", createJwtToken(adminUser))
                .content(objectMapper.writeValueAsString(enterpriseUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ErrorCode.FORBIDDEN.getCode()))
                .andReturn();

    }

    @Test
    public void shouldUpdateEnterpriseUserFailedWhenPhoneNumberIsMissing() throws Exception {
        adminUser.setEnterpriseId("111111");
        adminUser = userRepository.save(this.adminUser);
        enterpriseUser.setEnterpriseId("111111");
        enterpriseUser = userRepository.save(this.enterpriseUser);

        String userId = enterpriseUser.getId();

        enterpriseUser.setCellphone("");
        enterpriseUser.setTelephone("");

        mvc.perform(post("/enterprise-user/" + userId)
                .header("Authorization", createJwtToken(adminUser))
                .content(objectMapper.writeValueAsString(enterpriseUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.MISSING_PHONE_NUMBER.getCode()))
                .andReturn();
    }

    @Test
    public void shouldReturnEnterpriseUserById() throws Exception {
        adminUser.setEnterpriseId("111111");
        adminUser = userRepository.save(this.adminUser);
        enterpriseUser.setEnterpriseId("111111");
        enterpriseUser = userRepository.save(this.enterpriseUser);

        String userId = enterpriseUser.getId();

        mvc.perform(get("/enterprise-admin/users/" + userId)
                .header("Authorization", createJwtToken(adminUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void shouldGetEnterpriseUserFailedWithNoAuthorization() throws Exception {
        adminUser.setEnterpriseId("222222");
        adminUser = userRepository.save(this.adminUser);
        enterpriseUser.setEnterpriseId("111111");
        enterpriseUser = userRepository.save(this.enterpriseUser);

        String userId = enterpriseUser.getId();

        mvc.perform(get("/enterprise-admin/users/" + userId)
                .header("Authorization", createJwtToken(adminUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ErrorCode.FORBIDDEN.getCode()))
                .andReturn();
    }

    @Test
    public void shouldInitEnterpriseUserPassword() throws Exception {
        enterpriseUser.setEnterpriseId("111111");
        enterpriseUser = userRepository.save(this.enterpriseUser);

        String userId = enterpriseUser.getId();

        when(captchaService.isValidResponse(any(String.class), any(String.class))).thenReturn(true);

        mvc.perform(post("/enterprise-user/" + userId + "/init-password")
                .header("Authorization", createJwtToken(enterpriseUser))
                .content("{\"password\": \"P@ss123456\",\"originalPassword\": \"hello\", \"captchaId\": \"captchaId\",\"captcha\": \"captcha\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();
    }

    @Test
    public void shouldInitEnterpriseUserPasswordFailedWithAlreadyInitPassword() throws Exception {
        enterpriseUser.setEnterpriseId("111111");
        enterpriseUser.setResettable(true);
        enterpriseUser = userRepository.save(this.enterpriseUser);

        String userId = enterpriseUser.getId();

        when(captchaService.isValidResponse(any(String.class), any(String.class))).thenReturn(true);
        mvc.perform(post("/enterprise-user/" + userId + "/init-password")
                .header("Authorization", createJwtToken(enterpriseUser))
                .content("{\"password\": \"P@ss123456\",\"originalPassword\": \"hello\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ErrorCode.NO_AUTHORIZATION.getCode()))
                .andReturn();
    }

    @Test
    public void shouldInitEnterpriseUserPasswordFailedWithNoAuthorization() throws Exception {
        enterpriseUser = userRepository.save(this.enterpriseUser);

        String userId = enterpriseUser.getId();

        when(captchaService.isValidResponse(any(String.class), any(String.class))).thenReturn(true);
        mvc.perform(post("/enterprise-user/" + userId + "/init-password")
                .header("Authorization", createJwtToken(adminUser))
                .content("{\"password\": \"P@ss123456\",\"originalPassword\": \"hello\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void shouldInitEnterpriseUserPasswordFailedWhenPasswordIsMissing() throws Exception {
        enterpriseUser.setEnterpriseId("111111");
        enterpriseUser = userRepository.save(this.enterpriseUser);

        String userId = enterpriseUser.getId();

        when(captchaService.isValidResponse(any(String.class), any(String.class))).thenReturn(true);
        mvc.perform(post("/enterprise-user/" + userId + "/init-password")
                .header("Authorization", createJwtToken(enterpriseUser))
                .content("{\"password\": \"1111\",\"originalPassword\": \"hello\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_PASSWORD.getCode()))
                .andReturn();
    }

    @Test
    public void shouldInitEnterpriseUserPasswordFailedWhenOriginalPasswordIsMissing() throws Exception {
        enterpriseUser.setEnterpriseId("111111");
        enterpriseUser = userRepository.save(this.enterpriseUser);

        String userId = enterpriseUser.getId();

        when(captchaService.isValidResponse(any(String.class), any(String.class))).thenReturn(true);
        mvc.perform(post("/enterprise-user/" + userId + "/init-password")
                .header("Authorization", createJwtToken(enterpriseUser))
                .content("{\"password\": \"1111\",\"originalPassword\": \"wrong password\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ErrorCode.NO_AUTHORIZATION.getCode()))
                .andReturn();
    }


    @Test
    public void shouldResetEnterpriseUserPassword() throws Exception {
        Enterprise enterprise = enterpriseRepository.save(this.enterprise);
        adminUser.setEnterpriseId(enterprise.getId());
        adminUser = userRepository.save(adminUser);
        enterpriseUser.setEnterpriseId(enterprise.getId());
        enterpriseUser = userRepository.save(enterpriseUser);

        String userId = enterpriseUser.getId();

        mvc.perform(post("/enterprise-user/" + userId + "/reset-password")
                .header("Authorization", createJwtToken(adminUser))
                .content("{\"password\" : \"68auuuuuuuu\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();

        List<OperationLog> operationLogs = operationLogRepository.findAll();
        assertThat(operationLogs.size(), is(1));
        assertThat(operationLogs.get(0).getEnterpriseId(), is(enterprise.getId()));
        assertThat(operationLogs.get(0).getEnterpriseName(), is(enterprise.getName()));
        assertThat(operationLogs.get(0).getTargetUserId(), is(enterpriseUser.getId()));
        assertThat(operationLogs.get(0).getTargetUserName(), is(enterpriseUser.getUsername()));
        assertThat(operationLogs.get(0).getOperatorRole(), is("EnterpriseAdmin"));
        assertThat(operationLogs.get(0).getType(), is(OperationType.ResetEnterprisePassword));
    }

    @Test
    public void shouldRejectResetEnterpriseUserPasswordWhenPasswordLengthIsLessEight() throws Exception {
        adminUser.setEnterpriseId("111111");
        adminUser = userRepository.save(adminUser);
        enterpriseUser.setEnterpriseId("111111");
        enterpriseUser = userRepository.save(enterpriseUser);

        String userId = enterpriseUser.getId();

        mvc.perform(post("/enterprise-user/" + userId + "/reset-password")
                .header("Authorization", createJwtToken(adminUser))
                .content("{\"password\" : \"11\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void shouldRejectResetEnterpriseUserPasswordWhenNotBothContainAplhabetAndNumber() throws Exception {
        adminUser.setEnterpriseId("111111");
        adminUser = userRepository.save(adminUser);
        enterpriseUser.setEnterpriseId("111111");
        enterpriseUser = userRepository.save(enterpriseUser);

        String userId = enterpriseUser.getId();

        mvc.perform(post("/enterprise-user/" + userId + "/reset-password")
                .header("Authorization", createJwtToken(adminUser))
                .content("{\"password\" : \"1111111111\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void shouldResetEnterpriseAdminPassword() throws Exception {
        Enterprise enterprise = enterpriseRepository.save(this.enterprise);

        platformAdmin = userRepository.save(platformAdmin);
        adminUser.setEnterpriseId(enterprise.getId());
        User enterpriseAdmin = userRepository.save(adminUser);

        String enterpriseAdminId = enterpriseAdmin.getId();

        mvc.perform(post("/enterprise-admin/" + enterpriseAdminId + "/reset-password")
                .header("Authorization", createJwtToken(platformAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void shouldRejectResetEnterpriseAdminPasswordWithNoAuthorization() throws Exception {
        User enterpriseAdmin = userRepository.save(adminUser);

        String enterpriseAdminId = enterpriseAdmin.getId();

        mvc.perform(post("/enterprise-admin/" + enterpriseAdminId + "/reset-password")
                .header("Authorization", createJwtToken(enterpriseUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void shouldGetEnterpriseUserInformation() throws Exception {
        enterpriseUser = userRepository.save(enterpriseUser);

        String enterpriseUserId = enterpriseUser.getId();

        mvc.perform(get("/enterprise-user/" + enterpriseUserId)
                .header("Authorization", createJwtToken(enterpriseUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void shouldGetEnterpriseUserInformationFailedWithNoAuthorization() throws Exception {
        enterpriseUser = userRepository.save(enterpriseUser);

        String enterpriseUserId = enterpriseUser.getId();

        mvc.perform(get("/enterprise-user/" + enterpriseUserId)
                .header("Authorization", createJwtToken(adminUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }
}