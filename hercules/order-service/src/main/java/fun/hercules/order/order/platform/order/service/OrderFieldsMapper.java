package fun.hercules.order.order.platform.order.service;

import fun.hercules.order.order.common.errors.ErrorCode;
import fun.hercules.order.order.common.exceptions.InternalServerError;
import fun.hercules.order.order.platform.exports.BusinessOrder;
import fun.hercules.order.order.platform.exports.Mutable;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderFieldsMapper {

    private final List<Field> mutableFields;
    private final Class<? extends BusinessOrder> type;

    public OrderFieldsMapper(Class<? extends BusinessOrder> type) {
        this.type = type;
        mutableFields = getMutableFields(type);
    }

    public BusinessOrder map(BusinessOrder source, BusinessOrder target) {
        mutableFields.stream()
                .filter(field -> getFieldValue(field, source) != null)
                .forEach(field -> mapField(field, source, target));
        return target;
    }

    private List<Field> getMutableFields(Class<? extends BusinessOrder> type) {
        return getDeclaredFields(type).stream()
                .filter(field -> field.isAnnotationPresent(Mutable.class))
                .collect(Collectors.toList());
    }

    private List<Field> getDeclaredFields(Class type) {
        ArrayList<Field> fields = new ArrayList<>();
        while (!type.equals(Object.class)) {
            Arrays.stream(type.getDeclaredFields()).forEach(fields::add);
            type = type.getSuperclass();
        }
        return fields;
    }

    private void mapField(Field field, BusinessOrder source, BusinessOrder target) {
        if (Collection.class.isAssignableFrom(field.getType())) {
            Collection targetCollection = (Collection) getFieldValue(field, target);
            if (targetCollection != null) {
                targetCollection.clear();
                targetCollection.addAll((Collection) getFieldValue(field, source));
            } else {
                setFieldValue(field, target, getFieldValue(field, source));
            }
        } else {
            setFieldValue(field, target, getFieldValue(field, source));
        }
    }

    private Object getFieldValue(Field field, BusinessOrder order) {
        if (field != null) {
            PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(type, field.getName());
            Method readMethod = null;
            if (descriptor != null) {
                readMethod = descriptor.getReadMethod();
            }
            if (readMethod != null) {
                try {
                    return readMethod.invoke(order);
                } catch (Throwable ex) {
                    throw new InternalServerError(ErrorCode.SERVER_ERROR,
                            String.format("can't get filed %s of %s", field.getName(), order));
                }
            }
        }

        return null;
    }


    private void setFieldValue(Field field, BusinessOrder order, Object value) {
        if (field != null) {
            PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(type, field.getName());
            Method writeMethod = null;
            if (descriptor != null) {
                writeMethod = descriptor.getWriteMethod();
            }
            if (writeMethod != null) {
                try {
                    writeMethod.invoke(order, value);
                } catch (Throwable ex) {
                    throw new InternalServerError(ErrorCode.SERVER_ERROR,
                            String.format("can't set filed %s of %s", field.getName(), order));
                }
            }
        }
    }
}
