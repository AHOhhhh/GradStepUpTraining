package fun.hercules.user.enterprise.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fun.hercules.user.common.constants.Status;
import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.common.exceptions.BadRequestException;
import fun.hercules.user.common.exceptions.ForbiddenException;
import fun.hercules.user.common.exceptions.InternalServerError;
import fun.hercules.user.common.exceptions.NotFoundException;
import fun.hercules.user.enterprise.domain.Enterprise;
import fun.hercules.user.enterprise.domain.EnterpriseBase;
import fun.hercules.user.enterprise.domain.EnterpriseQualificationHistory;
import fun.hercules.user.enterprise.domain.PayMethod;
import fun.hercules.user.enterprise.dto.PayMethodConfigDTO;
import fun.hercules.user.enterprise.dto.PayMethodConfigItemDTO;
import fun.hercules.user.enterprise.repository.EnterpriseRepository;
import fun.hercules.user.enterprise.specification.EnterpriseSpecificationBuilder;
import fun.hercules.user.enterprise.util.PayMethodConfigHelper;
import fun.hercules.user.operation.domain.OperationType;
import fun.hercules.user.operation.service.OperationLogService;
import fun.hercules.user.support.PayMethodConfig;
import fun.hercules.user.support.PayMethodConfigItem;
import fun.hercules.user.user.domain.Role;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.security.CurrentUserContext;
import fun.hercules.user.user.service.UserService;
import fun.hercules.user.utils.CopyNonNullPropertyUtils;
import fun.hercules.user.enterprise.domain.Enterprise;
import fun.hercules.user.enterprise.domain.EnterpriseQualificationHistory;
import fun.hercules.user.enterprise.domain.PayMethod;
import fun.hercules.user.enterprise.repository.EnterpriseRepository;
import fun.hercules.user.enterprise.specification.EnterpriseSpecificationBuilder;
import fun.hercules.user.user.domain.Role;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.security.CurrentUserContext;
import fun.hercules.user.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static fun.hercules.user.common.errors.ErrorCode.ENTERPRISE_NOT_FOUND;
import static fun.hercules.user.common.errors.ErrorCode.FAILED_TO_CREATE_ENTERPRISE;
import static fun.hercules.user.enterprise.domain.EnterpriseBase.ValidationStatus.AuthorizationInProcess;

@Slf4j
@Service
public class EnterpriseService {

    public static final String VALIDATION_STATUS = "validationStatus";

    public static final String COMMENT = "comment";

    private final UserService userService;

    private final EnterpriseRepository enterpriseRepository;

    private final EnterpriseQualificationHistoryService qualificationHistoryService;

    private final CurrentUserContext userContext;

    private final OperationLogService operationLogService;

    private final PayMethodConfigHelper payMethodConfigHelper;

    private final PayMethodConfig payMethodConfig;


    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private CopyNonNullPropertyUtils beanUtils;

    @Autowired
    public EnterpriseService(UserService userService,
                             EnterpriseRepository enterpriseRepository,
                             EnterpriseQualificationHistoryService qualificationHistoryService,
                             CurrentUserContext userContext,
                             OperationLogService operationLogService,
                             PayMethodConfigHelper payMethodConfigHelper,
                             PayMethodConfig payMethodConfig) {
        this.userService = userService;
        this.enterpriseRepository = enterpriseRepository;
        this.qualificationHistoryService = qualificationHistoryService;
        this.userContext = userContext;
        this.operationLogService = operationLogService;
        this.payMethodConfigHelper = payMethodConfigHelper;
        this.payMethodConfig = payMethodConfig;
    }

    @Transactional
    @PreAuthorize("hasAuthority('EnterpriseAdmin') and @auth.isEnterpriseAdminAllowedSubmitEnterpriseInfo()")
    public String createEnterprise(Enterprise enterprise) {
        User user = userContext.getUser();

        try {
            // 保存企业信息状态为审核中
            enterprise.setValidationStatus(AuthorizationInProcess);
            enterprise.setStatus(Status.ENABLED);
            enterprise.setPayMethods(payMethodConfigHelper.getDefaultPayMethods(enterprise));
            Enterprise savedEnterprise = enterpriseRepository.save(enterprise);
            // 保存审核历史空记录，只记录企业信息，不包含审核状态
            qualificationHistoryService.saveHistoryFor(savedEnterprise);

            if (StringUtils.isEmpty(user.getEnterpriseId())) {
                user.setEnterpriseId(savedEnterprise.getId());
                userService.updateUser(user);
            }
            // 保存操作记录，仅在成功情况下记录操作日志
            saveOperationLog(savedEnterprise, OperationType.ApplyEnterpriseQualification);

            return savedEnterprise.getId();
        } catch (DataIntegrityViolationException ex) {
            log.warn("Fail to create enterprise: " + ex.getMessage());
            throw new BadRequestException(FAILED_TO_CREATE_ENTERPRISE,
                    String.format("Fail to create enterprise %s",
                            enterprise.getName()), ex);
        }
    }

    /**
     * 更新审核历史记录，重点在企业信息等，用于更新了企业信息等（用于多次审核及修改的场景）
     *
     * @param enterpriseId         企业ID
     * @param updateEnterpriseInfo 更新后的企业信息
     * @return 企业ID
     */
    @Transactional
    @PreAuthorize("hasAuthority('EnterpriseAdmin') and @auth.isEnterpriseAllowedReSubmit(#updateEnterpriseInfo)")
    public String updateEnterpriseQualification(String enterpriseId, Enterprise updateEnterpriseInfo) {
        Enterprise originalEnterprise = getById(enterpriseId);
        try {
            beanUtils.copyProperties(originalEnterprise, updateEnterpriseInfo);
        } catch (Exception e) {
            throw new InternalServerError(ErrorCode.INVALID_PARAMETER,
                    "copy non null value from update updateEnterpriseInfo failed");
        }

        originalEnterprise.setValidationStatus(AuthorizationInProcess);
        enterpriseRepository.save(originalEnterprise);
        // 保存审核历史空记录，只记录企业信息，没有审核状态
        qualificationHistoryService.saveHistoryFor(originalEnterprise);
        saveOperationLog(updateEnterpriseInfo, OperationType.UpdateEnterpriseQualification);
        return enterpriseId;
    }

    @PreAuthorize("@auth.isTheSameEnterpriseId(#enterpriseId) or hasAuthority('PlatformAdmin') or hasAuthority('PlatformAdmin')"
            + " or hasAuthority('PlatformService')")
    public Enterprise getEnterprise(String enterpriseId) {
        return getById(enterpriseId);
    }

    @PreAuthorize("@auth.isTheSameEnterpriseId(#enterpriseId) or hasAuthority('PlatformAdmin')")
    public List<EnterpriseQualificationHistory> getEnterpriseQualificationHistories(String enterpriseId) {
        return qualificationHistoryService.getEnterpriseQualificationHistories(enterpriseId);
    }

    public Enterprise getById(String enterpriseId) {
        return enterpriseRepository.findById(enterpriseId).orElseThrow(() ->
                new NotFoundException(ErrorCode.ENTERPRISE_NOT_FOUND,
                        String.format("can't find enterprise by enterpriseId " + "%s", enterpriseId)));
    }

    /**
     * 按照企业名称，审核状态，分页查询企业信息，列表使用
     *
     * @param name             企业名称
     * @param status           状态（禁用|非禁用）
     * @param validationStatus 审核状态
     * @param pageable         分页信息
     * @return 用map返回当前页的数据
     */
    @PreAuthorize("hasAuthority('PlatformAdmin') or hasAuthority('PlatformService')")
    public Page<Map> findAllByParams(String name, String status,
                                     String validationStatus,
                                     Pageable pageable) {
        EnterpriseSpecificationBuilder specificationBuilder = new EnterpriseSpecificationBuilder();
        Specification<Enterprise> spec = specificationBuilder.buildWithParams(name, status, validationStatus);

        Page<Enterprise> enterprises = enterpriseRepository.findAll(spec, pageable);


        List<User> admins = findAdminsForPagedEnterprise(enterprises);

        return enterprises.map(source -> {
            Map item = objectMapper.convertValue(source, Map.class);

            List<User> admin = admins.stream().filter(adminItem -> adminItem.getEnterpriseId().equals(source.getId()))
                    .collect(Collectors.toList());
            // 只设置为第一个admin
            if (admin != null && admin.size() > 0) {
                item.put("admin", admin.get(0));
            }
            return item;
        });
    }

    @PreAuthorize("hasAuthority('PlatformAdmin')")
    public User getEnterpriseAdmin(String enterpriseId) {
        return userService.getAdminByEnterpriseId(enterpriseId, Role.of(Role.Type.EnterpriseAdmin));
    }

    /**
     * 更新审核状态（审核动作）
     *
     * @param enterpriseId   企业ID
     * @param enterpriseInfo 企业信息
     * @return 企业对象
     */
    @Transactional
    @PreAuthorize("hasAuthority('PlatformAdmin')")
    public Enterprise updateEnterpriseQualificationStatus(String enterpriseId, Map<String, String> enterpriseInfo) {
        Enterprise enterprise = getById(enterpriseId);
        Enterprise.ValidationStatus status = Enterprise.ValidationStatus.valueOf(enterpriseInfo.get(VALIDATION_STATUS));
        enterprise.setValidationStatus(status);
        qualificationHistoryService.updateQualificationHistory(enterpriseId, status, enterpriseInfo.get(COMMENT));
        // 保存操作记录
        if (status.equals(Enterprise.ValidationStatus.Authorized)) {
            saveOperationLog(enterprise, OperationType.Authorized);
        } else {
            saveOperationLog(enterprise, OperationType.Unauthorized);
        }
        return enterprise;
    }

    public Enterprise getEnterpriseByName(String name) {
        return enterpriseRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(ENTERPRISE_NOT_FOUND));
    }

    /**
     * 更新企业状态：启用、禁用
     *
     * @param status       状态
     * @param enterpriseId 企业ID
     */
    @Transactional
    @PreAuthorize("hasAuthority('PlatformAdmin')")
    public void updateEnterpriseStatus(Status status, String enterpriseId) {
        Enterprise enterprise = getById(enterpriseId);
        enterprise.setStatus(status);
        enterpriseRepository.save(enterprise);
        // 更新企业下全部用户的状态（禁用企业则禁用所有用户，反之相同）
        userService.updateUsersStatusByEnterpriseId(status, enterpriseId);
        saveOperationLog(enterprise, status.equals(Status.ENABLED) ? OperationType.Enabled : OperationType.Disabled);
    }

    private void saveOperationLog(Enterprise enterprise, OperationType type) {
        operationLogService.save(enterprise, type);
    }

    private List<User> findAdminsForPagedEnterprise(Page<Enterprise> enterprises) {
        List<String> enterpriseIds = enterprises.getContent().stream().map(EnterpriseBase::getId).collect(Collectors
                .toList());

        return userService.findAdminsByEnterpriseIds(enterpriseIds);

    }

    @PreAuthorize("hasAuthority('PlatformAdmin') or hasAuthority('PlatformService')")
    public Page<Enterprise> listByIds(List<String> ids, Pageable pageable) {
        return new PageImpl<>(ids.stream().map(enterpriseRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList()));
    }

    /**
     * 查询企业支付方式（多种）
     *
     * @param enterpriseId 企业ID
     * @return 该企业的支付防止配置列表
     */
    @PreAuthorize("hasAnyAuthority('PlatformAdmin') or hasAuthority('PlatformService')")
    public List<PayMethodConfigDTO> getEnterprisePayMethods(String enterpriseId) {
        Enterprise enterprise = enterpriseRepository.findById(enterpriseId).orElse(null);
        if (enterprise == null) {
            throw new NotFoundException(ErrorCode.ENTERPRISE_NOT_FOUND);
        }

        return enterprise.getPayMethods().stream()
                .map(payMethod -> {

                    PayMethodConfigItem payMethodConfigItem = payMethodConfig.getItems().stream()
                            .filter(item -> item.getOrderType().equalsIgnoreCase(payMethod.getOrderType()))
                            .findAny()
                            .get();
                    log.info("enterpriseId : {}, orderType : {}, editable : {}, defaults: {}",
                            enterpriseId, payMethodConfigItem.getOrderType(),
                            payMethodConfigItem.getEditable(), payMethodConfigItem.getDefaults());
                    List<PayMethodConfigItemDTO> payMethodConfigItemDTOList = new ArrayList<>();

                    payMethod.getMethodsAsSet().stream()
                            .map(method -> PayMethodConfigItemDTO.builder()
                                    .name(method)
                                    .editable(payMethodConfigItem.getEditable().contains(method))
                                    .enabled(true)
                                    .build())
                            .forEach(payMethodConfigItemDTOList::add);

                    Arrays.stream(PayMethod.Type.values())
                            .filter(type -> !payMethod.getMethodsAsSet().contains(type.toString()))
                            .map(type -> PayMethodConfigItemDTO.builder()
                                    .name(type.toString())
                                    .editable(payMethodConfigItem.getEditable().contains(type.toString()))
                                    .enabled(false)
                                    .build())
                            .forEach(payMethodConfigItemDTOList::add);

                    return PayMethodConfigDTO.builder()
                            .enterpriseId(enterpriseId)
                            .orderType(payMethod.getOrderType())
                            .payMethods(payMethodConfigItemDTOList)
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 更新企业支付方式
     *
     * @param enterpriseId 企业ID
     * @param configs      支付方式配置信息，每种类型多种支付方式
     */
    @PreAuthorize("hasAnyAuthority('PlatformAdmin')")
    public void updateEnterprisePayMethods(String enterpriseId, Map<String, List<String>> configs) {

        verifyPayMethodOrderType(configs);

        Enterprise enterprise = enterpriseRepository.getOne(enterpriseId);
        if (enterprise == null) {
            throw new NotFoundException(ErrorCode.ENTERPRISE_NOT_FOUND);
        }

        // TODO: 2018/1/11 need to handle different orderTypes in database/config/request In the future.
        configs.forEach((orderType, newMethods) ->
                enterprise.getPayMethods().forEach(payMethod -> {
                    if (payMethod.getOrderType().equalsIgnoreCase(orderType)) {
                        verifyPayMethod(orderType, newMethods, payMethod.getMethodsAsSet());
                        payMethod.setMethods(newMethods);
                    }
                })
        );

        enterpriseRepository.save(enterprise);
        // 如果上一条保存出错，则不得记录operation log
        operationLogService.save(enterprise, OperationType.UpdatePayMethods);
    }

    /**
     * 检查企业选择的支付方式，目前系统每个供应商是否支持
     *
     * @param orderType  订单类型
     * @param newMethods 更新后的支付方式
     * @param oldMethods 更新前的支付方式
     */
    private void verifyPayMethod(String orderType, Collection<String> newMethods, Collection<String> oldMethods) {
        payMethodConfig.getItems().stream()
                .filter(item -> item.getOrderType().equalsIgnoreCase(orderType))
                .findAny()
                .ifPresent(item ->
                {
                    Collection<String> systemPayMethods = Arrays.stream(PayMethod.Type.values())
                            .map(Enum::toString)
                            .collect(Collectors.toList());

                    newMethods.forEach(newMethod -> {
                        boolean unknown = !systemPayMethods.contains(newMethod);
                        if (unknown) {
                            String message = orderType + "'s " + newMethod + " pay method is invalid!";
                            throw new ForbiddenException(ErrorCode.FORBIDDEN, message);
                        }
                    });

                    systemPayMethods.forEach(systemMethod -> {
                        boolean cannotEdit = !item.getEditable().contains(systemMethod);
                        boolean newExist = newMethods.contains(systemMethod);
                        boolean oldExist = oldMethods.contains(systemMethod);
                        if (cannotEdit && newExist != oldExist) {
                            String message = orderType + "'s " + systemMethod + " pay method cannot be edit!";
                            throw new ForbiddenException(ErrorCode.FORBIDDEN, message);
                        }
                    });
                });
    }

    private void verifyPayMethodOrderType(Map<String, List<String>> configs) {

        List<String> validOrderTypes = payMethodConfig.getItems().stream()
                .map(PayMethodConfigItem::getOrderType).collect(Collectors.toList());

        configs.keySet().forEach(orderType -> {
            boolean invalid = !validOrderTypes.contains(orderType.toLowerCase());
            if (invalid) {
                throw new ForbiddenException(ErrorCode.FORBIDDEN, "contains unknown orderType: " + orderType);
            }
        });

    }

    public void enableDeferredPayment(String enterpriseId) {
        Enterprise enterprise = getById(enterpriseId);
        List<PayMethod> methods = Optional.ofNullable(enterprise.getPayMethods()).orElse(Collections.EMPTY_LIST);
        payMethodConfig.getItems().stream().map(item -> item.getOrderType()).forEach(type -> {
            Optional<PayMethod> method = methods.stream().filter(payMethod -> payMethod.getOrderType().equalsIgnoreCase(type))
                    .findFirst();
            if (method.isPresent()) {
                Set<String> supportMethods = method.get().getMethodsAsSet();
                supportMethods.add(PayMethod.Type.DEFERMENT.toString());
                method.get().setMethods(supportMethods);
            } else {
                methods.add(new PayMethod(null, PayMethod.Type.DEFERMENT.toString(), type, enterprise));
            }
        });
        enterpriseRepository.save(enterprise);
    }
}
