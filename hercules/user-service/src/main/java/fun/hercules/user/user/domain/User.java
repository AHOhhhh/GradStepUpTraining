package fun.hercules.user.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import fun.hercules.user.common.constants.Status;
import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.common.exceptions.BadRequestException;
import fun.hercules.user.common.jpa.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
public class User extends EntityBase {
    @Id
    @GeneratedValue(generator = "user_id")
    @GenericGenerator(name = "user_id", strategy = "fun.hercules.user.user.service.UserIdGenerator")
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    @NotNull
    @Size(min = 6, max = 18)
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String username;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * 全名
     */
    @Size(max = 30)
    private String fullname;

    @Size(max = 88)
    @Pattern(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9]"
            + "(?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?"
            + "(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")
    private String email;

    /**
     * 手机号码
     */
    @Size(min = 1, max = 44)
    private String cellphone;

    /**
     * 电话号码
     */
    @Size(min = 1, max = 44)
    private String telephone;

    /**
     * 状态，是否禁用
     */
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.ENABLED;

    /**
     * 所属企业ID
     */
    private String enterpriseId;

    /**
     * 是否可以重置
     */
    private boolean resettable;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;

    /**
     * 权限列表
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_privilege",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id")
    )
    private Set<Privilege> privileges;

    /**
     * 是否具有指定角色
     *
     * @param role 角色类型
     * @return 是否具有指定角色的布尔值
     */
    // TODO - remove it
    public boolean haveSpecifyRole(Role.Type role) {
        return this.role.getType().equals(role);
    }

    /**
     * 检查企业ID不得为空
     */
    public void assertEnterpriseIdNotNull() {
        if (StringUtils.isAllEmpty(this.enterpriseId)) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST, "the user should pass enterprise approval first");
        }
    }

    public Set<Privilege> getPrivileges() {
        if (null == this.privileges) {
            return Collections.EMPTY_SET;
        }
        return privileges;
    }
}