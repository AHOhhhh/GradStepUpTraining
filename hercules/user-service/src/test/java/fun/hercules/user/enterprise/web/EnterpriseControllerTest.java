package fun.hercules.user.enterprise.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import fun.hercules.user.JUnitWebAppTest;
import fun.hercules.user.enterprise.domain.Enterprise;
import fun.hercules.user.enterprise.domain.EnterpriseQualificationHistory;
import fun.hercules.user.enterprise.repository.EnterpriseQualificationHistoryRepository;
import fun.hercules.user.enterprise.repository.EnterpriseRepository;
import fun.hercules.user.operation.domain.OperationLog;
import fun.hercules.user.operation.domain.OperationType;
import fun.hercules.user.operation.repository.OperationLogRepository;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.repository.UserRepository;
import fun.hercules.user.util.TestObjectBuilder;
import fun.hercules.user.utils.JsonUtils;
import fun.hercules.user.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static fun.hercules.user.util.FileUtils.loadTestSuiteResource;
import static fun.hercules.user.utils.AuthorizationUtil.createJwtToken;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Transactional
@RunWith(SpringRunner.class)
@JUnitWebAppTest
public class EnterpriseControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private EnterpriseQualificationHistoryRepository historyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestObjectBuilder testObjectBuilder;

    @Autowired
    private OperationLogRepository operationLogRepository;

    private Enterprise enterprise;

    private User enterpriseAdmin;

    private User savedPlatformAdmin;

    @Before
    public void setUp() throws Exception {
        enterpriseAdmin = testObjectBuilder.loadUserFromJsonData("enterprise_admin_user.json");
        enterpriseAdmin = userRepository.save(enterpriseAdmin);
        savedPlatformAdmin = testObjectBuilder.loadUserFromJsonData("platform_admin_user.json");
        savedPlatformAdmin = userRepository.save(savedPlatformAdmin);
        enterprise = testObjectBuilder.loadEnterpriseFromJson("enterprise_qualification.json");
    }

    @Test
    public void shouldCreateEnterpriseIfValidationPass() throws Exception {
        MvcResult result = mvc.perform(post("/enterprises")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseAdmin))
                .accept(MediaType.APPLICATION_JSON)
                .content(loadTestSuiteResource("/enterprise/enterprise_qualification.json"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String createdEnterpriseId = result.getResponse().getContentAsString();

        mvc.perform(get("/enterprises/" + createdEnterpriseId)
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(savedPlatformAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uniformSocialCreditCode").value("12330400068392995K"));

        mvc.perform(get("/enterprises/histories/" + createdEnterpriseId)
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(savedPlatformAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        List<OperationLog> operationLogs = operationLogRepository.findAll();
        Enterprise enterprise = enterpriseRepository.findByName(this.enterprise.getName()).get();

        assertThat(operationLogs.size(), is(1));
        assertThat(operationLogs.get(0).getEnterpriseId(), is(enterprise.getId()));
        assertThat(operationLogs.get(0).getEnterpriseName(), is(enterprise.getName()));
        assertThat(operationLogs.get(0).getTargetUserId(), is(""));
        assertThat(operationLogs.get(0).getTargetUserName(), is(""));
        assertThat(operationLogs.get(0).getOperatorRole(), is("EnterpriseAdmin"));
        assertThat(operationLogs.get(0).getType(), is(OperationType.ApplyEnterpriseQualification));

    }

    @Test
    public void shouldUpdateEnterpriseQualificationSuccessfully() throws Exception {
        String json = loadTestSuiteResource("/enterprise/enterprise_qualification.json");
        Enterprise enterprise = testObjectBuilder.loadEnterpriseFromJson("enterprise_qualification.json");

        MvcResult result = mvc.perform(post("/enterprises")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseAdmin))
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String createdEnterpriseId = result.getResponse().getContentAsString();

        enterprise.setId(createdEnterpriseId);
        enterprise.setRegistrationAddress("update address");
        enterprise.setValidationStatus(Enterprise.ValidationStatus.Unauthorized);

        List<OperationLog> operationLogs = operationLogRepository.findAll();
        Enterprise savedEnterprise = enterpriseRepository.findByName(this.enterprise.getName()).get();

        assertThat(operationLogs.size(), is(1));
        assertThat(operationLogs.get(0).getEnterpriseId(), is(savedEnterprise.getId()));
        assertThat(operationLogs.get(0).getEnterpriseName(), is(savedEnterprise.getName()));
        assertThat(operationLogs.get(0).getTargetUserId(), is(""));
        assertThat(operationLogs.get(0).getTargetUserName(), is(""));
        assertThat(operationLogs.get(0).getOperatorRole(), is("EnterpriseAdmin"));
        assertThat(operationLogs.get(0).getType(), is(OperationType.ApplyEnterpriseQualification));

        mvc.perform(post("/enterprises/" + createdEnterpriseId)
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseAdmin))
                .accept(MediaType.APPLICATION_JSON)
                .content(JsonUtils.getMapper().writeValueAsString(enterprise))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Optional<Enterprise> updatedEnterprise = enterpriseRepository.findById(createdEnterpriseId);
        assertTrue(updatedEnterprise.get().getRegistrationAddress().equals("update address"));
        assertTrue(updatedEnterprise.get().getValidationStatus().equals(Enterprise.ValidationStatus.AuthorizationInProcess));

        operationLogs = operationLogRepository.findAll();
        assertThat(operationLogs.size(), is(2));
        assertThat(operationLogs.get(1).getEnterpriseId(), is(enterprise.getId()));
        assertThat(operationLogs.get(1).getEnterpriseName(), is(enterprise.getName()));
        assertThat(operationLogs.get(1).getTargetUserId(), is(""));
        assertThat(operationLogs.get(1).getTargetUserName(), is(""));
        assertThat(operationLogs.get(1).getOperatorRole(), is("EnterpriseAdmin"));
        assertThat(operationLogs.get(1).getType(), is(OperationType.UpdateEnterpriseQualification));

        mvc.perform(get("/enterprises/histories/" + createdEnterpriseId)
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(savedPlatformAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void shouldFindEnterpriseIfAlreadyExisted() throws Exception {
        Enterprise enterprise = enterpriseRepository.save(this.enterprise);
        mvc.perform(get("/enterprises/" + enterprise.getId())
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(savedPlatformAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldFindAllEnterprisesWithEmptyParams() throws Exception {
        String id = enterpriseRepository.save(enterprise).getId();
        enterpriseAdmin.setEnterpriseId(id);
        userRepository.save(enterpriseAdmin);

        mvc.perform(get("/enterprises")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(savedPlatformAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("测试企业"));
    }

    @Test
    public void shouldFindAllEnterprisesWithName() throws Exception {
        Enterprise factory = testObjectBuilder.loadEnterpriseFromJson("enterprise_qualification.json");
        factory.setName("测试工厂");

        Enterprise enterprise = enterpriseRepository.save(this.enterprise);
        enterpriseAdmin.setEnterpriseId(enterprise.getId());
        userRepository.save(enterpriseAdmin);
        enterpriseRepository.save(factory);

        mvc.perform(get("/enterprises?name=企业")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(savedPlatformAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("测试企业"));
    }

    @Test
    public void shouldGetLatestEnterpriseByPlatformUser() throws Exception {
        Enterprise enterprise = enterpriseRepository.save(this.enterprise);

        mvc.perform(get("/enterprises/" + enterprise.getId())
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(savedPlatformAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldListEnterpriseByPlatformUser() throws Exception {
        Enterprise enterprise1 = enterpriseRepository.save(enterprise);
        Enterprise enterprise2 = enterpriseRepository.save(enterprise);

        mvc.perform(get("/enterprises")
                .param("ids", enterprise1.getId(), enterprise2.getId())
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(savedPlatformAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(2)));
    }

    @Test
    public void shouldGetLatestEnterpriseByAdminWithSameEnterpriseId() throws Exception {
        Enterprise enterprise = enterpriseRepository.save(this.enterprise);
        enterpriseAdmin.setEnterpriseId(enterprise.getId());
        User enterpriseAdmin = userRepository.save(this.enterpriseAdmin);

        mvc.perform(get("/enterprises/" + enterprise.getId())
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetLatestEnterpriseFailedByAdminWithDifferentEnterpriseId() throws Exception {
        Enterprise enterprise = enterpriseRepository.save(this.enterprise);

        mvc.perform(get("/enterprises/" + enterprise.getId())
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldGetEnterpriseAdminByEnterpriseId() throws Exception {
        String id = enterpriseRepository.save(this.enterprise).getId();
        enterpriseAdmin.setEnterpriseId(id);
        userRepository.save(enterpriseAdmin);

        mvc.perform(get("/enterprises/" + id + "/admin")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(savedPlatformAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetEnterpriseAdminFailedWhenCurrentOperatorHasNoAuthority() throws Exception {
        enterpriseRepository.save(enterprise);

        mvc.perform(get("/enterprises/fake" + "/admin")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldUpdateEnterpriseStatusAndCommentByEnterpriseId() throws Exception {
        MvcResult result = mvc.perform(post("/enterprises")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseAdmin))
                .accept(MediaType.APPLICATION_JSON)
                .content(loadTestSuiteResource("/enterprise/enterprise_qualification.json"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String enterpriseId = result.getResponse().getContentAsString();

        Map enterpriseInfo = new HashMap();
        enterpriseInfo.put("validationStatus", Enterprise.ValidationStatus.Authorized);
        enterpriseInfo.put("comment", "pass authorized");

        mvc.perform(post("/enterprises/" + enterpriseId + "/approve")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(savedPlatformAdmin))
                .content(objectMapper.writeValueAsString(enterpriseInfo))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<Enterprise> enterprise = enterpriseRepository.findById(enterpriseId);
        List<EnterpriseQualificationHistory> histories = historyRepository
                .findByEnterpriseIdOrderByCreatedAtDesc(enterpriseId);


        assertThat(enterprise.get().getValidationStatus(), is(Enterprise.ValidationStatus.Authorized));
        assertThat(histories.size(), is(1));
        assertTrue(histories.get(0).getComment().equals("pass authorized"));
    }

    @Test
    public void shouldUpdateEnterpriseStatusAndCommentFailedWhenCurrentOperatorHasNoAuthority() throws Exception {
        Enterprise enterprise = enterpriseRepository.save(this.enterprise);
        Map enterpriseInfo = new HashMap();
        enterpriseInfo.put("validationStatus", "Authorized");
        enterpriseInfo.put("comment", "pass authorized");

        mvc.perform(post("/enterprises/" + enterprise.getId() + "/approve")
                .content(objectMapper.writeValueAsString(enterpriseInfo))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldGetForbiddenStatusWhenHasHasNoAuthority() throws Exception {
        mvc.perform(get("/enterprises" + "/121212")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(enterpriseAdmin))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}