package fun.hercules.order.order.business.wms.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import fun.hercules.order.order.platform.exports.EntityBase;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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
@Where(clause = EntityBase.SKIP_DELETED_CLAUSE)
@SQLDelete(sql = "UPDATE wms_order_type SET deleted = true WHERE id = ? AND version = ?")
public class WmsOrderType extends EntityBase {

    @Transient
    private Type type;

    @JsonCreator
    public static WmsOrderType of(String value) {
        return of(Type.valueOf(value));
    }

    public static WmsOrderType of(Type value) {
        return new WmsOrderType(value);
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


    @Transient
    public Type getType() {
        return type;
    }


    @Override
    public String toString() {
        return getName();
    }

    public enum Type {
        Open("开通"),
        Renew("续费"),
        Recharge("充值");

        private final String description;

        Type(String description) {

            this.description = description;
        }

        public String getDescription() {
            return description;
        }

    }

}
