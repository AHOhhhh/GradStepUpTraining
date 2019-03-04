package fun.hercules.user.user.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import fun.hercules.user.common.jpa.EntityBase;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Immutable
@EqualsAndHashCode(of = "type", callSuper = false)
@Access(AccessType.PROPERTY)
public class Privilege extends EntityBase {

    /**
     * 类型
     */
    @Transient
    private Type type;

    @JsonCreator
    public static Privilege of(String value) {
        return of(Type.valueOf(value));
    }

    public static Privilege of(Type value) {
        return new Privilege(value);
    }

    @Transient
    public Type getType() {
        return type;
    }

    @Id
    @Column(name = "id")
    public Integer getId() {
        return type.ordinal();
    }

    @Column(name = "id")
    public void setId(Integer id) {
        type = Type.values()[id];
    }


    @Override
    public String toString() {
        return getName();
    }

    /**
     * 权限名称，即类型
     *
     * @return 权限名称
     */
    @JsonValue
    @Column(name = "name")
    public String getName() {
        return type.toString();
    }

    @Column(name = "name")
    public void setName(String value) {
    }

    /**
     * 权限描述
     *
     * @return 权限描述
     */
    @Column(name = "description")
    public String getDescription() {
        return type.getDescription();
    }

    @Column(name = "description")
    public void setDescription(String value) {
    }

    /**
     * 类型，即所有支持的权限列表，与项目中支持的功能相关
     */
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
