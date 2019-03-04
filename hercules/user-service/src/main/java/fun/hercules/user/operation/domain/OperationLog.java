package fun.hercules.user.operation.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import fun.hercules.user.common.jpa.EntityBase;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 操作日志，只记录对业务数据的操作（因此只记录成功操作的步骤），用于变更追踪
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Immutable
@EqualsAndHashCode(callSuper = true)
@Table(name = "operation_log")
public class OperationLog extends EntityBase {

    @Transient
    String typeDescription;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    private String enterpriseId;

    private String enterpriseName;

    @ManyToOne
    @JoinColumn(name = "operator_id")
    @JsonIgnore
    private User operator;

    /**
     * 操作人用户名
     */
    @Transient
    private String operatorName;

    /**
     * 操作人角色
     */
    private String operatorRole;

    /**
     * 目标用户ID
     */
    private String targetUserId;

    /**
     * 目标用户名
     */
    private String targetUserName;

    /**
     * 索引号
     */
    @Transient
    private int index;

    @NonNull
    @Enumerated(EnumType.STRING)
    private OperationType type;

}
