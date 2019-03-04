package fun.hercules.user.enterprise.service;

import fun.hercules.user.common.constants.Status;
import fun.hercules.user.enterprise.domain.Enterprise;
import fun.hercules.user.enterprise.repository.EnterpriseRepository;
import fun.hercules.user.operation.service.OperationLogService;
import fun.hercules.user.user.service.UserService;
import fun.hercules.user.util.TestObjectBuilder;
import fun.hercules.user.common.constants.Status;
import fun.hercules.user.enterprise.domain.Enterprise;
import fun.hercules.user.enterprise.repository.EnterpriseRepository;
import fun.hercules.user.operation.service.OperationLogService;
import fun.hercules.user.user.service.UserService;
import fun.hercules.user.util.TestObjectBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnterpriseServiceTest {

    @Mock
    private EnterpriseRepository enterpriseRepository;

    @Mock
    private UserService userService;

    @Mock
    private OperationLogService operationLogService;

    @InjectMocks
    private EnterpriseService enterpriseService;

    @InjectMocks
    private TestObjectBuilder testObjectBuilder;

    private String enterpriseId;

    private Enterprise enterprise;

    @Before
    public void setup() throws IOException {
        enterpriseId = UUID.randomUUID().toString();

        enterprise = testObjectBuilder.loadEnterpriseFromJson("enterprise_qualification.json");
        enterprise.setId(enterpriseId);
    }

    @Test
    public void shouldCanUpdateEnterpriseStatus() throws Exception {
        when(enterpriseRepository.findById(enterpriseId)).thenReturn(Optional.of(enterprise));
        enterpriseService.updateEnterpriseStatus(Status.ENABLED, enterpriseId);
        verify(enterpriseRepository).save(enterprise);
        assertThat(enterprise.getStatus(), is(Status.ENABLED));
        verify(userService).updateUsersStatusByEnterpriseId(Status.ENABLED, enterpriseId);
    }
}