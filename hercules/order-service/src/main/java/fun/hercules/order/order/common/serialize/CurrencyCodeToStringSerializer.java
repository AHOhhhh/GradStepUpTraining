package fun.hercules.order.order.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import fun.hercules.order.payment.transaction.domain.CurrencyCode;

import java.io.IOException;

public class CurrencyCodeToStringSerializer extends JsonSerializer<CurrencyCode> {
    @Override
    public void serialize(CurrencyCode value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.getCode());
    }
}
