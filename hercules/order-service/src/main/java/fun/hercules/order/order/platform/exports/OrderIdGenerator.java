package fun.hercules.order.order.platform.exports;


import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Date;

public class OrderIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        BusinessOrderBase order = (BusinessOrderBase) object;
        // 1位业务类型(见BusinessType)+9位unix时间戳(精确到毫秒)+4位随机数
        return String.format("%s%s%s",
                getBusinessIdPrefix(order),
                getTimestamp(),
                getRandom()
        );
    }


    private String getBusinessIdPrefix(BusinessOrderBase order) {
        String prefix = order.getBusinessType().prefix();
        if (!StringUtils.isNumeric(prefix)) {
            throw new IllegalArgumentException(String.format("business type must be integer"));
        }
        int prefixValue = Integer.parseInt(prefix);
        if (prefixValue < 1 || prefixValue > 9) {
            throw new IllegalArgumentException(String.format("business type must between 1 ~ 9"));
        }
        return prefix;
    }

    private String getTimestamp() {
        return String.format("%09d", (new Date().getTime()) % 1000000000);
    }

    private String getRandom() {
        return RandomStringUtils.randomNumeric(4);
    }

    private String getUserIdHashCode(BusinessOrderBase order) {
        int userSuffix = Math.abs(order.getUserId().hashCode() % 1000);
        return String.format("%03d", userSuffix);
    }
}
