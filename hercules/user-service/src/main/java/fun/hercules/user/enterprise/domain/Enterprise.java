package fun.hercules.user.enterprise.domain;

import fun.hercules.user.common.jpa.EntityBase;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

@Data
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = true)
@Where(clause = EntityBase.SKIP_DELETED_CLAUSE)
@ToString(callSuper = true)
public class Enterprise extends EnterpriseBase {

}
