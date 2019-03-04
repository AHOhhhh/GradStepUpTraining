package fun.hercules.user.common.jpa;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.Instant;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
/** Remark: explicitly declare the AccessType to fucking FIELD to
 * ensure the version field works for Role & Privilege
 */
@Access(AccessType.FIELD)
public abstract class EntityBase implements Auditable {
    public static final String SKIP_DELETED_CLAUSE = "deleted = false";

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    @Version
    @JsonIgnore
    private Integer version = 0;

    @JsonIgnore
    private boolean deleted = false;
}
