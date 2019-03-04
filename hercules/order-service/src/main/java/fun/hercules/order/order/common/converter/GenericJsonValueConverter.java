package fun.hercules.order.order.common.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import fun.hercules.order.order.utils.JsonUtils;

import javax.persistence.AttributeConverter;

public abstract class GenericJsonValueConverter<T> implements AttributeConverter<T, String> {

    private TypeReference<T> typeRef;

    public GenericJsonValueConverter(TypeReference<T> typeRef) {
        this.typeRef = typeRef;
    }

    @Override
    public String convertToDatabaseColumn(T rules) {
        return JsonUtils.marshal(rules);
    }

    @Override
    public T convertToEntityAttribute(String dbData) {
        return null == dbData ? null : JsonUtils.unmarshal(dbData, typeRef);
    }
}