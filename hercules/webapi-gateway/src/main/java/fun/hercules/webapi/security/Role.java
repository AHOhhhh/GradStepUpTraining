package fun.hercules.webapi.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role {
    private Type type;
    private Collection<Privilege> privileges;

    @JsonCreator
    public static Role of(String value) {
        return of(Type.valueOf(value));
    }

    public static Role of(Type value) {
        return new Role(value, Collections.EMPTY_SET);
    }

    public enum Type {
        PlatformService("platform service user"),
        PlatformAdmin("platform admin"),
        EnterpriseAdmin("enterprise admin"),
        EnterpriseUser("enterprise user"),
        OperationAdmin("operation admin"),
        IndividualUser("individual user");

        private final String description;

        Type(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
