package fun.hercules.user.user.service;

import com.google.common.base.Functions;
import com.google.common.collect.Sets;
import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.common.exceptions.NotFoundException;
import fun.hercules.user.user.domain.Privilege;
import fun.hercules.user.user.domain.Role;
import fun.hercules.user.user.repository.RoleRepository;
import fun.hercules.user.user.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@DependsOn("privilegeService")
public class RoleService {
    private RoleRepository roleRepository;


    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initializeRoles() {
        Set<Role> currentRoles = Sets.newHashSet(roleRepository.findAll());

        List<Role> missingRoles = getPredefinedRoles().stream()
                .filter(role -> !currentRoles.contains(role))
                .collect(Collectors.toList());
        // 如果数据库未保存预定义的角色，即数据库的少于程序要用到的，则保存到数据库
        if (!missingRoles.isEmpty()) {
            roleRepository.save(missingRoles).forEach(role -> log.info("add missing role {}", role));
        }
    }

    /**
     * 预定义的角色
     *
     * @return 角色列表
     */
    private List<Role> getPredefinedRoles() {
        Map<Role.Type, HashSet<Privilege>> privileges = Arrays.stream(Role.Type.values())
                .collect(Collectors.toMap(Functions.identity(), type -> new HashSet<Privilege>()));
        privileges.get(Role.Type.PlatformService).add(Privilege.of(Privilege.Type.AllPrivileges));
        privileges.get(Role.Type.PlatformAdmin).add(Privilege.of(Privilege.Type.OrderManagementPrivilege));
        privileges.get(Role.Type.EnterpriseAdmin).add(Privilege.of(Privilege.Type.EnterpriseUserManagementPrivilege));
        privileges.get(Role.Type.EnterpriseUser).add(Privilege.of(Privilege.Type.OrderAccessPrivilege));

        return privileges.entrySet().stream()
                .map(rolePrivileges -> new Role(rolePrivileges.getKey()))
                .collect(Collectors.toList());
    }

    public Optional<Role> findByType(Role.Type type) {
        return Optional.ofNullable(roleRepository.findOne(type.ordinal()));
    }

    public Role getByType(Role.Type type) {
        return findByType(type).orElseThrow(() -> new NotFoundException(ErrorCode.ROLE_NOT_FOUND,
                String.format("role %s not found", type)));
    }
}
