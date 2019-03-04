package fun.hercules.order.order.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import fun.hercules.order.order.platform.order.model.PayChannel;

import java.io.IOException;

public class PayChannelSerializer extends JsonSerializer<PayChannel> {
    @Override
    public void serialize(PayChannel value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.getValue());
    }
}
