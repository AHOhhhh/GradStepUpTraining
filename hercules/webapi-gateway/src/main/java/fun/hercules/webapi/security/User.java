package fun.hercules.webapi.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String id;

    private String username;

    private String fullname;

    private String email;

    private String cellphone;

    private String telephone;

    private String status;

    private String enterpriseId;

    private boolean resettable;

    private String role;

    private Set<String> privileges;

    public boolean isEnterpriseAdmin() {
        return role.equalsIgnoreCase(Role.Type.EnterpriseAdmin.toString());
    }

    public boolean isEnterpriseUser() {
        return role.equalsIgnoreCase(Role.Type.EnterpriseUser.toString());
    }

    public boolean isPlatformAdmin() {
        return role.equalsIgnoreCase(Role.Type.PlatformAdmin.toString());
    }

    public boolean isOperationAdmin() {
        return role.equalsIgnoreCase(Role.Type.OperationAdmin.toString());
    }


}
