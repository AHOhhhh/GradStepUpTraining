package fun.hercules.webapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import fun.hercules.webapi.client.UserClient;

import java.util.Map;

public class PlatformAuthenticationFilter extends AuthenticationFilter {

    public PlatformAuthenticationFilter(UserClient userClient, ObjectMapper objectMapper, Map<String, Object> configs) {
        super(userClient, objectMapper, configs);
        setFilterProcessesUrl("/webapi/admin/login");
    }

    @Override
    protected boolean isLegalRole(User user) {
        return user.isPlatformAdmin() || user.isOperationAdmin();
    }
}
