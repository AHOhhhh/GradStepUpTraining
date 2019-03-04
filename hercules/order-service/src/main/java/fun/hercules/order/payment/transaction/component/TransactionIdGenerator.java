package fun.hercules.order.payment.transaction.component;

import fun.hercules.order.order.common.constants.ConfigProperties;
import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public abstract class TransactionIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        // 2位随机数+15位时间(yyMMddHHmmssSSS)+4位用户Id哈希值后3位+4位随机数
        // 10~99
        return String.format("%s%s%s%s",
                getRandom(2),
                getTimestamp(),
                getUserIdHashCode(object),
                getRandom(4)
        );
    }

    private String getTimestamp() {
        return DateTimeFormatter.ofPattern("yyMMddHHmmssSSS")
                .withZone(ConfigProperties.getInstance().getTimeZone())
                .format(Instant.now());
    }

    private String getRandom(int count) {
        return RandomStringUtils.randomNumeric(count);
    }

    public abstract String getUserIdHashCode(Object object);
}
