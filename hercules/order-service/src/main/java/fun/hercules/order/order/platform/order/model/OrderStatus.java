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
@SQLDelete(sql = "UPDATE order_status SET deleted = true WHERE id = ? AND version = ?")
public class OrderStatus extends EntityBase {

    @Transient
    private Type type;

    @JsonCreator
    public static OrderStatus of(String value) {
        return of(Type.valueOf(value));
    }

    public static OrderStatus of(Type value) {
        return new OrderStatus(value);
    }

    @Transient
    public Type getType() {
        return type;
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


    @Override
    public String toString() {
        return getName();
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

    public enum Type {
        Submitted("submitted"),
        WaitForPay("wait for pay"),
        Paid("paid"),
        OfflinePaidAwaitingConfirm("OfflinePaidAwaitingConfirm"),
        OfflinePaidNotConfirmed("OfflinePaidNotConfirmed"),
        Audited("audited"),
        ConfirmingPayment("confirming payment"),
        Closed("order finished"),
        Cancelled("order has been cancelled(not deleted)"),
        OrderTracking("order tracking");


        private final String description;

        Type(String description) {

            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
