package fun.hercules.user.enterprise.domain;

import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.common.exceptions.InternalServerError;
import fun.hercules.user.common.jpa.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;

/**
 * 企业认证历史，包含企业信息，及当时审核的评论
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = true)
@Where(clause = EntityBase.SKIP_DELETED_CLAUSE)
public class EnterpriseQualificationHistory extends EnterpriseBase {

    /**
     * 企业ID
     */
    @Column
    private String enterpriseId;

    /**
     * 评论内容
     */
    @Column
    private String comment;

    @Builder
    private EnterpriseQualificationHistory(Enterprise enterprise, String enterpriseId, String comment) {

        BeanUtilsBean utilsBean = new BeanUtilsBean();
        try {
            utilsBean.copyProperties(this, enterprise);
        } catch (Exception e) {
            throw new InternalServerError(ErrorCode.INVALID_PARAMETER,
                    "copy value from enterprise failed when build enterprise qualification history");
        }
        this.enterpriseId = enterpriseId;
        this.comment = comment;
    }
}
