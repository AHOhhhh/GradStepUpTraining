package fun.hercules.user.enterprise.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fun.hercules.user.common.jpa.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 消息提醒
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = {"updatedAt", "createdBy", "updatedBy"})
public class Notification extends EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    /**
     * 企业ID
     */
    @NotNull
    private String enterpriseId;

    /**
     * 提醒类型
     */
    @NotNull
    @Enumerated(value = EnumType.STRING)
    private NotificationType notificationType;

    /**
     * 订单ID
     */
    @NotNull
    private String orderId;

    /**
     * 标题
     */
    @NotNull
    private String name;

    /**
     * 订单类型
     */
    @NotNull
    private String orderType;

    /**
     * 订单信息
     */
    private String orderInfo;

    /**
     * 状态
     */
    private String status;

    /**
     * 消息内容
     */
    @NotNull
    private String description;

    /**
     * 服务类型，支持多种，逗号分隔
     */
    private String serviceTypes;

    @Override
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss'Z'", timezone = "UTC")
    public Instant getCreatedAt() {
        return super.getCreatedAt();
    }

    @JsonGetter(value = "serviceTypes")
    public List<String> getServiceTypeList() {
        if (StringUtils.isNotEmpty(serviceTypes)) {
            return Arrays.asList(serviceTypes.split(","));
        }
        return new ArrayList<>();
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType.toLowerCase();
    }
}
