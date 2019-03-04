package fun.hercules.order.order.platform.order.utils;


import fun.hercules.order.order.common.errors.ErrorCode;
import fun.hercules.order.order.common.exceptions.InternalServerError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

@Slf4j
public class ReflectUtil {

    public static Object getFieldValueByFieldName(String fieldName, Object object) {
        PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(object.getClass(), fieldName);
        if (sourcePd != null) {
            Method readMethod = sourcePd.getReadMethod();
            if (readMethod != null) {
                try {
                    return readMethod.invoke(object);
                } catch (Throwable ex) {
                    throw new InternalServerError(
                            ErrorCode.SERVER_ERROR, "meet error when use reflect to get field value " + fieldName);
                }
            }
        }
        log.error("No such field \"{}\" in this class and its super classes: {}", fieldName, object.getClass().getName());
        throw new InternalServerError(ErrorCode.MISSING_FIELD, "missing " + fieldName);
    }
}
