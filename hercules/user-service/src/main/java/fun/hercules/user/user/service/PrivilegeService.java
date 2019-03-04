package fun.hercules.user.user.service;

import com.google.common.collect.Sets;
import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.common.exceptions.NotFoundException;
import fun.hercules.user.user.domain.Privilege;
import fun.hercules.user.user.repository.PrivilegeRepository;
import fun.hercules.user.user.domain.Privilege;
import fun.hercules.user.user.repository.PrivilegeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PrivilegeService {
    private PrivilegeRepository repository;

    public PrivilegeService(PrivilegeRepository repository) {
        this.repository = repository;
    }

    // 初始化权限，仅app启动时执行
    @PostConstruct
    public void initializePrivileges() {
        Set<Privilege> currentPrivileges = Sets.newHashSet(repository.findAll());

        List<Privilege> missingPrivileges = Arrays.stream(Privilege.Type.values())
                .filter(type -> !currentPrivileges.contains(Privilege.of(type)))
                .map(type -> Privilege.of(type))
                .collect(Collectors.toList());

        // 如果数据库未保存Privilege.Type定义的权限，即数据库的少于程序要用到的，则保存到数据库
        if (!missingPrivileges.isEmpty()) {
            repository.save(missingPrivileges).forEach(privilege -> log.info("add missing privilege {}", privilege));
        }
    }

    public Privilege findByType(Privilege.Type type) {
        return Optional.ofNullable(repository.findOne(type.ordinal())).orElseThrow(() -> new NotFoundException(ErrorCode.PRIVILEGE_NOT_FOUND));
    }

}
