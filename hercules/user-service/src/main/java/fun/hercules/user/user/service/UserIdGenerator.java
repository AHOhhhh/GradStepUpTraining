package fun.hercules.user.user.service;

import fun.hercules.user.user.domain.Role;
import fun.hercules.user.user.domain.User;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;
import org.springframework.util.StringUtils;

import java.io.Serializable;

public class UserIdGenerator extends UUIDGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        User user = (User) object;
        if (!StringUtils.isEmpty(user.getId()) && user.haveSpecifyRole(Role.Type.PlatformService)) {
            return user.getId();
        } else {
            return super.generate(session, object);
        }
    }
}
