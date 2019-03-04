package fun.hercules.user.enterprise.domain;

import fun.hercules.user.common.constants.Status;
import fun.hercules.user.common.jpa.EntityBase;
import fun.hercules.user.utils.SensitiveDataEncoder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Where(clause = EntityBase.SKIP_DELETED_CLAUSE)
public abstract class EnterpriseBase extends EntityBase {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(36)")
    protected String id;

    @NotNull
    @Size(min = 1, max = 100)
    protected String name;

    /**
     * 统一社会信用代码
     */
    @NotNull
    @Pattern(regexp = "^([0-9A-Za-z]){18}$")
    protected String uniformSocialCreditCode;

    /**
     * 统一社会信用执照代码
     */
    @NotNull
    @Size(min = 1, max = 152)
    protected String certificateForUniformSocialCreditCode;

    /**
     * 营业执照编号
     */
    @Pattern(regexp = "[0-9]{15}")
    protected String businessLicenseNumber;

    /**
     * 营业执照
     */
    @Size(min = 1, max = 152)
    protected String certificateForBusinessLicense;

    /**
     * 纳税人识别号
     */
    @Pattern(regexp = "[0-9a-zA-Z]{20}")
    protected String taxPayerNumber;

    /**
     * 纳税人资质
     */
    @Size(min = 1, max = 152)
    protected String certificateForTaxRegistration;

    /**
     * 组织机构编码
     */
    @Pattern(regexp = "[a-zA-Z0-9]{8}-[a-zA-Z0-9]")
    protected String organizationCode;

    /**
     * 公司资质
     */
    @Size(min = 1, max = 152)
    protected String certificateForOrganization;

    /**
     * 注册地址
     */
    @Size(min = 1, max = 152)
    protected String registrationAddress;

    /**
     * 法人名称
     */
    @NotNull
    @Size(min = 1, max = 30)
    protected String artificialPersonName;

    /**
     * 法人联系方式
     */
    @NotNull
    @Size(min = 1, max = 44)
    protected String artificialPersonContact;

    /**
     * 审核结果
     */
    @Enumerated(EnumType.STRING)
    protected ValidationStatus validationStatus;

    /**
     * 账号状态，是否禁用
     */
    @Enumerated(EnumType.STRING)
    protected Status status = Status.ENABLED;

    /**
     * 支付方式，支持多种支付方式
     */
    @OneToMany(mappedBy = "enterprise", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<PayMethod> payMethods;

    @Override
    public String toString() {
        return "EnterpriseBase{"
                + "id='" + id + '\''
                + ", name='" + SensitiveDataEncoder.encode(name) + '\''
                + ", uniformSocialCreditCode='" + SensitiveDataEncoder.encode(uniformSocialCreditCode) + '\''
                + ", certificateForUniformSocialCreditCode='" + SensitiveDataEncoder.encode(certificateForUniformSocialCreditCode) + '\''
                + ", businessLicenseNumber='" + SensitiveDataEncoder.encode(businessLicenseNumber) + '\''
                + ", certificateForBusinessLicense='" + SensitiveDataEncoder.encode(certificateForBusinessLicense) + '\''
                + ", taxPayerNumber='" + SensitiveDataEncoder.encode(taxPayerNumber) + '\''
                + ", certificateForTaxRegistration='" + SensitiveDataEncoder.encode(certificateForTaxRegistration) + '\''
                + ", organizationCode='" + SensitiveDataEncoder.encode(organizationCode) + '\''
                + ", certificateForOrganization='" + SensitiveDataEncoder.encode(certificateForOrganization) + '\''
                + ", registrationAddress='" + SensitiveDataEncoder.encode(registrationAddress) + '\''
                + ", artificialPersonName='" + SensitiveDataEncoder.encode(artificialPersonName) + '\''
                + ", artificialPersonContact='" + SensitiveDataEncoder.encode(artificialPersonContact) + '\''
                + ", validationStatus=" + validationStatus
                + ", status=" + status
                + ", payMethods=" + payMethods
                + '}';
    }

    public enum ValidationStatus {
        /**
         * 未认证
         */
        Unauthorized,
        /**
         * 认证中
         */
        AuthorizationInProcess,
        /**
         * 已认证
         */
        Authorized
    }
}
