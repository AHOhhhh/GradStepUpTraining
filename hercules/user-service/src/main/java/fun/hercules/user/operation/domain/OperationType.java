package fun.hercules.user.operation.domain;

public enum OperationType {

    // Platform admin operation
    Enabled("启用企业"),
    Disabled("停用企业"),
    ResetPassword("重置企业admin密码"),
    Authorized("认证企业资质:通过"),
    Unauthorized("认证企业资质:未通过"),
    UpdatePayMethods("设置支付模式"),

    //Enterprise admin operation
    ApplyEnterpriseQualification("申请认证企业资质"),
    UpdateEnterpriseQualification("更新企业资质"),
    UpdateEnterpriseAdminInfo("编辑企业管理员信息"),
    CreateEnterpriseUser("创建企业用户"),
    UpdateEnterpriseUser("编辑企业用户"),
    DisableEnterpriseUser("停用企业用户"),
    EnableEnterpriseUser("启用企业用户"),
    ResetEnterprisePassword("重置企业用户密码");

    private String description;

    OperationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
