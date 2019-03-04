package fun.hercules.order.order.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import fun.hercules.order.order.platform.order.model.PayMethod;

import java.io.IOException;

public class PayMethodSerializer extends JsonSerializer<PayMethod> {
    @Override
    public void serialize(PayMethod value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.getValue());
    }
}
