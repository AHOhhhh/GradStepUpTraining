package fun.hercules.webapi.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Privilege {
    private Type type;

    @JsonCreator
    public static Privilege of(String value) {
        return of(Type.valueOf(value));
    }

    public static Privilege of(Type value) {
        return new Privilege(value);
    }

    public Type getType() {
        return type;
    }

    public Integer getId() {
        return type.ordinal();
    }

    public void setId(Integer id) {
        type = Type.values()[id];
    }


    @Override
    public String toString() {
        return getName();
    }

    @JsonValue
    public String getName() {
        return type.toString();
    }

    public void setName(String value) {
    }

    public String getDescription() {
        return type.getDescription();
    }

    public void setDescription(String value) {
    }

    public enum Type {
        // for PlatformService
        AllPrivileges("superman"),
        // for PlatformAdmin
        OrderManagementPrivilege("update order critical data(e.g. price)"),
        // for EnterpriseAdmin,
        EnterpriseUserManagementPrivilege("create enterprise users"),
        // for EnterpriseUser
        OrderAccessPrivilege("create, view, update, delete normal orders"),
        AcgOrderAccessPrivilege("All privileges for acg order"),
        YzraVendorAccessPrivilege("All privileges for yzra vendor"),
        WmsOrderAccessPrivilege("All privileges for wms"),
        WmsVendorAccessPrivilege("All privileges for wms vendor"),
        ScfOrderAccessPrivilege("All privileges for scf order"),
        SshVendorAccessPrivilege("All privileges for ssh vendor"),
        MwpOrderAccessPrivilege("All privileges for mwp order"),
        MwpVendorAccessPrivilege("All privileges for mwp vendor");

        private final String description;

        Type(String description) {

            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

}
