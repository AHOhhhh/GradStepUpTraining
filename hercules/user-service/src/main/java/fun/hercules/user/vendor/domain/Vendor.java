package fun.hercules.user.vendor.domain;

import fun.hercules.user.common.jpa.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Where(clause = EntityBase.SKIP_DELETED_CLAUSE)
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE vendor SET deleted = true WHERE id = ? AND version = ?")
public class Vendor extends EntityBase {

    @Id
    @Size(max = 36)
    @NotNull
    private String id;

    @Size(max = 50)
    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private BusinessType businessType;

    @NotNull
    private String payMethods;
}
