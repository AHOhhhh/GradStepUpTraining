package fun.hercules.user.user.service;

import fun.hercules.user.common.constants.Status;
import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.common.exceptions.BadRequestException;
import fun.hercules.user.common.exceptions.ConflictException;
import fun.hercules.user.common.exceptions.NotFoundException;
import fun.hercules.user.enterprise.domain.Enterprise;
import fun.hercules.user.enterprise.domain.EnterpriseBase;
import fun.hercules.user.enterprise.repository.EnterpriseRepository;
import fun.hercules.user.enterprise.util.PayMethodConfigHelper;
import fun.hercules.user.operation.domain.OperationType;
import fun.hercules.user.operation.service.OperationLogService;
import fun.hercules.user.user.domain.Privilege;
import fun.hercules.user.user.domain.Role;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.repository.UserRepository;
import fun.hercules.user.user.security.CurrentUserContext;
import fun.hercules.user.utils.AuthorizationUtil;
import fun.hercules.user.utils.Pair;
import fun.hercules.user.utils.captcha.CaptchaService;
import fun.hercules.user.user.repository.UserRepository;
import fun.hercules.user.user.security.CurrentUserContext;
import fun.hercules.user.user.security.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static fun.hercules.user.user.domain.Role.Type.EnterpriseAdmin;
import static fun.hercules.user.user.domain.Role.Type.EnterpriseUser;
import static fun.hercules.user.user.domain.Role.Type.PlatformAdmin;
import static fun.hercules.user.user.security.SecurityConstants.TOKEN_PREFIX;
import static fun.hercules.user.utils.VerifyUserUtils.checkOriginalPassword;
import static fun.hercules.user.utils.VerifyUserUtils.checkResetAbility;
import static fun.hercules.user.utils.VerifyUserUtils.verifyFullname;
import static fun.hercules.user.utils.VerifyUserUtils.verifyInitPassword;
import static fun.hercules.user.utils.VerifyUserUtils.verifyPhoneNumber;
import static fun.hercules.user.utils.VerifyUserUtils.verifyResetPassword;

@Slf4j
@Service
public class UserService {
    private final RoleService roleService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final CurrentUserContext userContext;

    private final EnterpriseRepository enterpriseRepository;

    private final PrivilegeService privilegeService;

    private final OperationLogService operationLogService;

    private final PayMethodConfigHelper payMethodConfigHelper;

    private final CaptchaService captchaService;

    private String initPassword;


    public UserService(UserRepository userRepository, RoleService roleService,
                       PasswordEncoder passwordEncoder,
                       CurrentUserContext userContext,
                       EnterpriseRepository enterpriseRepository,
                       @Value("${init.enterprise.admin.password}") String initPassword,
                       PrivilegeService privilegeService, OperationLogService operationLogService,
                       PayMethodConfigHelper payMethodConfigHelper, CaptchaService captchaService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userContext = userContext;
        this.enterpriseRepository = enterpriseRepository;
        this.initPassword = initPassword;
        this.privilegeService = privilegeService;
        this.operationLogService = operationLogService;
        this.payMethodConfigHelper = payMethodConfigHelper;
        this.captchaService = captchaService;
    }

    public User getById(String id) {
        return findById(id).orElseThrow(() ->
                new NotFoundException(ErrorCode.USER_NOT_FOUND, String.format("can't find user by id %s", id)));
    }


    public Optional<User> findById(String id) {
        return Optional.ofNullable(userRepository.findOne(id));
    }

    public User create(User user) {
        // 检查用户是否已经存在，如存在则抛异常
        validateUserExists(user);
        return createUser(user);
    }

    private void enableUser(User user) {
        user.setStatus(Status.ENABLED);
    }

    /**
     * 创建企业用户
     *
     * @param user 需要创建的用户
     * @return 创建成功的用户
     */
    @PreAuthorize("hasAuthority('EnterpriseAdmin')")
    public User createEnterpriseUser(User user) {
        User operator = userContext.getUser();
        operator.assertEnterpriseIdNotNull();
        user.setEnterpriseId(operator.getEnterpriseId());
        // 启用用户
        enableUser(user);
        User createdUser = create(user);
        Enterprise enterprise = enterpriseRepository.findById(operator.getEnterpriseId()).get();
        // 记录操作日志
        operationLogService.saveWithUser(enterprise, createdUser, OperationType.CreateEnterpriseUser);
        return createdUser;
    }

    @PreAuthorize("hasAuthority('EnterpriseAdmin') and @auth.isTheSameEnterpriseId(#user.enterpriseId)")
    public User updateStatus(User user, String status) {
        user.setStatus(Status.valueOf(status));
        User newStatusUser = userRepository.save(user);
        OperationType operationType = status.equalsIgnoreCase("Disabled") ? OperationType.DisableEnterpriseUser : OperationType.EnableEnterpriseUser;
        Enterprise enterprise = enterpriseRepository.findById(userContext.getUser().getEnterpriseId()).get();
        operationLogService.saveWithUser(enterprise, newStatusUser, operationType);

        return newStatusUser;
    }

    /**
     * 创建用户并登录（生成token并返回)
     *
     * @param user           需要创建的用户
     * @param headerConsumer 用于接收用户的token
     * @return 创建成功的用户
     */
    public User createAndLogin(User user, Consumer<String> headerConsumer) {
        user = create(user);
        // 通过consumer传回调用处
        headerConsumer.accept(createToken(user));
        return user;
    }

    private String createToken(User user) {
        return SecurityConstants.TOKEN_PREFIX + AuthorizationUtil.createJwtToken(user);
    }

    private void encodePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    /**
     * 给用户绑定权限
     *
     * @param user 需要绑定权限的用户
     */
    private void bindPrivilege(User user) {
        if (null == user.getPrivileges()) {
            if (user.getRole().equals(Role.of(Role.Type.EnterpriseAdmin))) {
                user.setPrivileges(new HashSet<Privilege>() {
                    {
                        add(Privilege.of(Privilege.Type.EnterpriseUserManagementPrivilege));
                    }
                });
            } else {
                user.setPrivileges(new HashSet<Privilege>() {
                    {
                        add(Privilege.of(Privilege.Type.OrderAccessPrivilege));
                    }
                });
            }
        }
        Set<Privilege> privileges = user.getPrivileges().stream()
                .map(privilege -> privilegeService.findByType(privilege.getType())).collect(Collectors.toSet());
        user.setPrivileges(privileges);
    }

    /**
     * 给用户绑定角色
     *
     * @param user 需要绑定角色的用户
     */
    private void bindRoles(User user) {
        user.setRole(roleService.getByType(user.getRole().getType()));
    }

    public User getByUsername(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND,
                        String.format("can't find user by name %s", username)));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public void updateUser(User user) {
        userRepository.save(user);
    }

    /**
     * PlatformServiceUser is used for create enum table, for auditing
     */
    @PostConstruct
    @Transactional
    public void createPlatformServiceUser() {
        prepareUser(userContext.getPlatformServiceUser());
    }

    // TODO - for test
    @PostConstruct
    @Transactional
    public void createTestAdminUser() {

        Enterprise enterprise = prepareEnterprise();

        prepareUser(User.builder()
                .username("twadmin")
                .password("p@ssword1")
                .enterpriseId(enterprise.getId())
                .fullname("ThoughtWorks Administrator")
                .role(Role.of(EnterpriseAdmin))
                .resettable(true)
                .privileges(new HashSet<Privilege>() {
                    {
                        add(Privilege.of(Privilege.Type.EnterpriseUserManagementPrivilege));
                    }
                })
                .cellphone("1234567890")
                .email("twadmin@thoughtworks.com")
                .build());

        prepareUser(User.builder()
                .username("twuser")
                .password("p@ssword1")
                .enterpriseId(enterprise.getId())
                .fullname("ThoughtWorks User")
                .role(Role.of(EnterpriseUser))
                .privileges(new HashSet<Privilege>() {
                    {
                        add(Privilege.of(Privilege.Type.OrderAccessPrivilege));
                    }
                })
                .resettable(true)
                .cellphone("1234567890")
                .email("twuser@thoughtworks.com")
                .build());

        prepareUser(User.builder()
                .username("platformadmin")
                .password("p@ssword1")
                .fullname("Test PlatformAdmin")
                .role(Role.of(PlatformAdmin))
                .privileges(new HashSet<Privilege>() {
                    {
                        add(Privilege.of(Privilege.Type.AllPrivileges));
                    }
                })
                .cellphone("1234567890")
                .email("admin@thoughtworks.com")
                .build());
    }

    private Enterprise prepareEnterprise() {
        Enterprise enterprise = new Enterprise();
        enterprise.setName("ThoughtWorks");
        enterprise.setUniformSocialCreditCode("12330400068392995K");
        enterprise.setCertificateForUniformSocialCreditCode("121212");
        enterprise.setBusinessLicenseNumber("111111111111111");
        enterprise.setCertificateForBusinessLicense("121212");
        enterprise.setTaxPayerNumber("12345678901234567890");
        enterprise.setCertificateForTaxRegistration("121212");
        enterprise.setOrganizationCode("jjjkskfs-8");
        enterprise.setCertificateForOrganization("121212");
        enterprise.setRegistrationAddress("天谷八路");
        enterprise.setArtificialPersonName("张三丰");
        enterprise.setArtificialPersonContact("软件新城");
        enterprise.setValidationStatus(EnterpriseBase.ValidationStatus.valueOf("Authorized"));
        enterprise.setStatus(Status.ENABLED);

        Optional<Enterprise> enterpriseOptional = enterpriseRepository.findByName(enterprise.getName());
        if (enterpriseOptional.isPresent()) {
            Enterprise savedEnterprise = enterpriseOptional.get();
            if (CollectionUtils.isEmpty(savedEnterprise.getPayMethods())) {
                savedEnterprise.setPayMethods(payMethodConfigHelper.getDefaultPayMethods(savedEnterprise));
                enterpriseRepository.save(savedEnterprise);
            }
        }

        return enterpriseOptional.orElseGet(() -> enterpriseRepository.save(enterprise));
    }

    private void prepareUser(User user) {
        Optional<User> userExisted = findByUsername(user.getUsername());
        if (!userExisted.isPresent()) {
            userExisted = Optional.of(create(user));
            log.info("create userExisted {}:{}", userExisted.get().getUsername(), userExisted.get().getId()
            );
        } else {
            userExisted.get().setEnterpriseId(user.getEnterpriseId());
        }
    }

    /**
     * 获取企业下所有用户，分页查询
     *
     * @param enterpriseId 企业ID
     * @param pageable     分页信息
     * @return 用户列表
     */
    @PreAuthorize("hasAuthority('EnterpriseAdmin') and @auth.isTheSameEnterpriseId(#enterpriseId)")
    public Page<User> getByEnterpriseId(String enterpriseId, Pageable pageable) {
        User operator = userContext.getUser();
        if (!StringUtils.equals(operator.getEnterpriseId(), enterpriseId)) {
            return new PageImpl<>(Collections.emptyList(), null, 0);
        }

        Page<User> users = userRepository.findByEnterpriseId(enterpriseId, pageable);

        List<User> filterUsers = users.getContent()
                .stream()
                .filter(user -> user.haveSpecifyRole(Role.Type.EnterpriseUser)).collect(Collectors.toList());

        return new PageImpl(filterUsers, pageable, users.getTotalElements());
    }

    public User getAdminByEnterpriseId(String enterpriseId, Role role) {
        return userRepository.findByEnterpriseIdAndRole(enterpriseId, role).orElseThrow(() ->
                new NotFoundException(ErrorCode.USER_NOT_FOUND, "Enterprise admin not found"));
    }

    public User verifyPassword(Pair loginInfo) {
        Optional<User> user = findByUsername(loginInfo.getKey());

        if (!user.isPresent()) {
            return null;
        }

        return passwordEncoder.matches(loginInfo.getValue(), user.get().getPassword()) ? user.get() : null;
    }

    @PreAuthorize("hasAuthority('EnterpriseAdmin') and @auth.isTheSameEnterpriseId(#originalUser.enterpriseId)")
    public void updateEnterpriseUser(User originalUser, User user) {
        verifyPhoneNumber(user);
        updateUserInfo(originalUser, user);
        Enterprise enterprise = enterpriseRepository.findById(userContext.getUser().getEnterpriseId()).get();
        operationLogService.saveWithUser(enterprise, user, OperationType.UpdateEnterpriseUser);
    }

    @PreAuthorize("hasAuthority('EnterpriseAdmin') and @auth.isTheSameEnterpriseId(#originalUser.enterpriseId)")
    private User updateUserInfo(User originalUser, User user) {
        originalUser.setFullname(user.getFullname());
        originalUser.setCellphone(StringUtils.isEmpty(user.getCellphone()) ? null : user.getCellphone());
        originalUser.setTelephone(StringUtils.isEmpty(user.getTelephone()) ? null : user.getTelephone());
        originalUser.setEmail(user.getEmail());

        return userRepository.save(originalUser);
    }

    public User updateEnterpriseAdmin(String id, User user) {
        verifyFullname(user);
        verifyPhoneNumber(user);
        User updateUserInfo = updateUserInfo(getById(id), user);
        Enterprise enterprise = enterpriseRepository.findById(updateUserInfo.getEnterpriseId()).get();
        operationLogService.saveWithUser(enterprise, updateUserInfo, OperationType.UpdateEnterpriseAdminInfo);
        return updateUserInfo;
    }

    @PreAuthorize("hasAuthority('EnterpriseAdmin') and @auth.isTheSameEnterpriseId(#user.enterpriseId)")
    public User getEnterpriseUser(User user) {
        return user;
    }

    public void resetEnterpriseUserPassword(String id, String password) {
        User originalUser = getById(id);
        updateEnterpriseUserPassword(originalUser, password);
        Enterprise enterprise = enterpriseRepository.findById(userContext.getUser().getEnterpriseId()).get();
        operationLogService.saveWithUser(enterprise, originalUser, OperationType.ResetEnterprisePassword);
    }

    @PreAuthorize("hasAuthority('EnterpriseAdmin') and @auth.isTheSameEnterpriseId(#originalUser.enterpriseId)")
    private void updateEnterpriseUserPassword(User originalUser, String password) {
        verifyResetPassword(password);
        resetPassword(originalUser, password);
    }

    private void resetPassword(User originalUser, String password) {
        originalUser.setPassword(password);
        originalUser.setResettable(false);
        encodePassword(originalUser);

        userRepository.save(originalUser);
    }

    @PreAuthorize("@auth.isTheSameUserId(#originalUser.id)")
    public void initPassword(User originalUser, String originalPassword, String password, String captchaId, String captcha) {
        checkOriginalPassword(originalUser, originalPassword, passwordEncoder);
        checkResetAbility(originalUser);
        verifyInitPassword(password);
        verifyCaptcha(captchaId, captcha);
        originalUser.setPassword(password);
        originalUser.setResettable(true);
        encodePassword(originalUser);

        userRepository.save(originalUser);
    }

    @PreAuthorize("hasAuthority('PlatformAdmin')")
    public void resetEnterpriseAdminPassword(String id) {
        User user = getById(id);
        resetPassword(user, initPassword);
        Optional<Enterprise> enterpriseOptional = enterpriseRepository.findById(user.getEnterpriseId());
        Enterprise enterprise = enterpriseOptional.orElseThrow(() -> new NotFoundException(ErrorCode.ENTERPRISE_NOT_FOUND));
        operationLogService.save(enterprise, OperationType.ResetPassword);
    }

    @PreAuthorize("hasAuthority('EnterpriseUser')")
    public User getEnterpriseUserById(String id) {
        return getById(id);
    }

    @PreAuthorize("hasAuthority('PlatformAdmin') or hasAuthority('PlatformService')")
    public List<User> findAdminsByEnterpriseIds(List<String> enterpriseIds) {
        List<User> admins = userRepository.findByEnterpriseIdIsInAndRole(enterpriseIds, Role.of(Role.Type.EnterpriseAdmin));
        return admins;
    }

    @PreAuthorize("hasAuthority('PlatformAdmin')")
    public void updateUsersStatusByEnterpriseId(Status status, String enterpriseId) {
        userRepository.updateStatus(status, enterpriseId);
    }

    private String truncate(String value, int size) {
        return value.length() <= size ? value : value.substring(0, size);
    }

    @PreAuthorize("hasAuthority('PlatformAdmin')")
    public User createAdmin(User user) {
        validate(user);
        return createUser(user);
    }

    private User createUser(User user) {
        try {
            bindRoles(user);
            bindPrivilege(user);
            enableUser(user);
            encodePassword(user);
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            log.warn(String.format("failed to create user %s", user), e);
            throw new BadRequestException(ErrorCode.FAILED_TO_CREATE_USER,
                    String.format("failed to create user %s", user.getUsername()), e);
        }
    }

    private void validate(User user) {
        validateUserExists(user);
    }

    private void validateUserExists(User user) {
        if (findByUsername(user.getUsername()).isPresent()) {
            throw new ConflictException(ErrorCode.USER_ALREADY_EXISTS,
                    String.format("user %s already exists", user.getUsername()));
        }
    }

    private void verifyCaptcha(String captchaId, String captcha) {
        if (!captchaService.isValidResponse(captchaId, captcha.toUpperCase())) {
            throw new BadRequestException(ErrorCode.INVALID_CAPTCHA);
        }
    }
}
