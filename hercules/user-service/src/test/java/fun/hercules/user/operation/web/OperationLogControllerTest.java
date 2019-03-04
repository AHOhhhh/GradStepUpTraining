package fun.hercules.user.operation.web;

import fun.hercules.user.JUnitWebAppTest;
import fun.hercules.user.operation.domain.OperationLog;
import fun.hercules.user.operation.domain.OperationType;
import fun.hercules.user.operation.repository.OperationLogRepository;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.repository.UserRepository;
import fun.hercules.user.util.TestObjectBuilder;
import fun.hercules.user.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static fun.hercules.user.utils.AuthorizationUtil.createJwtToken;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Transactional
@RunWith(SpringRunner.class)
@JUnitWebAppTest
public class OperationLogControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestObjectBuilder testObjectBuilder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OperationLogRepository operationLogRepository;

    private User savedPlatformAdmin;

    private User enterpriseAdmin;

    @Before
    public void setUp() throws Exception {
        enterpriseAdmin = userRepository.save(testObjectBuilder.loadUserFromJsonData("enterprise_admin_user.json"));
        savedPlatformAdmin = userRepository.save(testObjectBuilder.loadUserFromJsonData("platform_admin_user.json"));
        createOperationLog("enterprise-id-1", "enterprise-name-1", this.enterpriseAdmin, "", "", OperationType.CreateEnterpriseUser);
        createOperationLog("enterprise-id-2", "enterprise-name-2", this.savedPlatformAdmin, "", "", OperationType.Authorized);
        createOperationLog("enterprise-id-3", "enterprise-name-3", this.enterpriseAdmin, "user 2", "user-id-2", OperationType.DisableEnterpriseUser);
    }

    @Test
    public void shouldShowAllOperationLogsForEnterpriseAdmin() throws Exception {
        mvc.perform(get("/user-operations?role=EnterpriseAdmin&enterpriseId=enterprise-id-3")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(savedPlatformAdmin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].enterpriseId").value("enterprise-id-3"))
                .andExpect(jsonPath("$.content[0].operatorName").value(enterpriseAdmin.getFullname()))
                .andExpect(jsonPath("$.content[0].targetUserName").value("user 2"))
                .andExpect(jsonPath("$.content[0].targetUserId").value("user-id-2"))
                .andExpect(jsonPath("$.content[0].type").value(OperationType.DisableEnterpriseUser.name()));
    }

    @Test
    public void shouldShowAllOperationLogsForPlatformAdmin() throws Exception {
        mvc.perform(get("/user-operations?role=PlatformAdmin&enterpriseName=enterprise-name-2")
                .header(HttpHeaders.AUTHORIZATION, createJwtToken(savedPlatformAdmin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))

                .andExpect(jsonPath("$.content[0].enterpriseId").value("enterprise-id-2"))
                .andExpect(jsonPath("$.content[0].operatorName").value(savedPlatformAdmin.getFullname()))
                .andExpect(jsonPath("$.content[0].targetUserName").value(""))
                .andExpect(jsonPath("$.content[0].targetUserId").value(""))
                .andExpect(jsonPath("$.content[0].type").value(OperationType.Authorized.name()));
    }

    private void createOperationLog(String enterpriseId, String enterpriseName, User enterpriseAdmin, String targetUserName, String targetUserId, OperationType operationType) {
        OperationLog operationLog = OperationLog.builder()
                .enterpriseId(enterpriseId)
                .enterpriseName(enterpriseName)
                .operator(enterpriseAdmin)
                .targetUserName(targetUserName)
                .targetUserId(targetUserId)
                .operatorRole(enterpriseAdmin.getRole().getName())
                .type(operationType)
                .build();

        operationLogRepository.save(operationLog);
    }
}
