package fun.hercules.user.enterprise.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import fun.hercules.user.common.jpa.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Collection;
import java.util.Set;

/**
 * 企业支付方式，保存企业时会保存相应的数据，与企业为一对多关系
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Data
@ToString(exclude = "enterprise")
@EqualsAndHashCode(callSuper = true)
public class PayMethod extends EntityBase {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(36)")
    protected String id;

    /**
     * 方式，逗号分隔
     */
    private String methods;

    private String orderType;

    /**
     * 关联企业
     */
    @ManyToOne
    @JoinColumn(name = "enterprise_id")
    @JsonIgnore
    private Enterprise enterprise;

    public Set<String> getMethodsAsSet() {
        return Sets.newHashSet(methods.split(","));
    }

    public void setMethods(Collection<String> values) {
        this.methods = String.join(",", values);
    }

    public enum Type {
        ONLINE, OFFLINE, DEFERMENT
    }
}
