package fun.hercules.order.order.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import fun.hercules.order.order.platform.order.model.OrderBill;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class VendorToStringSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String transferredValue = OrderBill.vendorMap.get(value);
        if (StringUtils.isEmpty(transferredValue)) {
            transferredValue = value;
        }
        gen.writeString(transferredValue);
    }
}
