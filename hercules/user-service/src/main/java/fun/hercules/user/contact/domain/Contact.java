package fun.hercules.user.contact.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fun.hercules.user.common.jpa.EntityBase;
import fun.hercules.user.contact.validators.ContactPhoneConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 联系人，映射数据库entity
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ContactPhoneConstraint
public class Contact extends EntityBase {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    @NotNull(message = "Name should not be null")
    @Size(max = 30)
    private String name;

    /**
     * 企业ID
     */
    @NotNull(message = "Enterprise id should not be null")
    private String enterpriseId;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 地址
     */
    @Size(max = 200)
    private String address;

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
     * 国家
     */
    @NotNull(message = "Country should not be null")
    private String country;

    /**
     * 国家简称
     */
    @NotNull(message = "Country abbreviation should not be null")
    private String countryAbbr;

    /**
     * 省
     */
    private String province;

    /**
     * 省ID
     */
    private String provinceId;

    /**
     * 市
     */
    private String city;

    /**
     * 市ID
     */
    private String cityId;

    /**
     * 区
     */
    private String district;

    /**
     * 区ID
     */
    private String districtId;

    /**
     * 邮政编码
     */
    private String postcode;

    private String email;

    /**
     * 是否默认联系人
     */
    private boolean isDefault;

    /**
     * 是否删除
     */
    private boolean deleted;

}