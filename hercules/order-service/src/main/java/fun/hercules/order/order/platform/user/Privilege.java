package fun.hercules.order.order.platform.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "type", callSuper = false)
public class Privilege {

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

    @JsonValue
    @Column(name = "name")
    public String getName() {
        return type.toString();
    }

    @Column(name = "name")
    public void setName(String value) {
    }

    @Column(name = "description")
    public String getDescription() {
        return type.getDescription();
    }

    @Column(name = "description")
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
        @OrderTypeAccess(value = {"acg"})
        AcgOrderAccessPrivilege("All privileges for acg order"),
        @OrderTypeAccess(value = {"wms"})
        WmsOrderAccessPrivilege("All privileges for wms");



        private final String description;

        Type(String description) {

            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }


}
