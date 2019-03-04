package fun.hercules.order.order.platform.order.model;

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
@SQLDelete(sql = "UPDATE order_type SET deleted = true WHERE id = ? AND version = ?")
public class OrderType extends EntityBase {

    @Transient
    private Type type;

    @JsonCreator
    public static OrderType of(String value) {
        return of(Type.valueOf(value.toUpperCase()));
    }

    public static OrderType of(Type value) {
        return new OrderType(value);
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
        return type.toString().toLowerCase();
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
        WMS("warehouse management system"),
        ACG("air cargo");

        private final String description;

        Type(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public String getValue() {
            return name().toLowerCase();
        }
    }

}
