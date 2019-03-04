package fun.hercules.order.order.business.wms.constants;

import fun.hercules.order.order.utils.JsonUtils;
import fun.hercules.order.order.utils.ResourceUtils;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = {"telephone"})
public class WmsDefaultInformation {
    private static final WmsDefaultInformation wmsDefaultInformation;

    static {
        wmsDefaultInformation = JsonUtils.unmarshal(
                ResourceUtils.loadResource("/wms/order-description.json"),
                WmsDefaultInformation.class);
    }

    private String describe;

    private Integer maxPrice;

    private Integer minPrice;

    private String telephone;

    public static WmsDefaultInformation getInstance() {
        return wmsDefaultInformation;
    }
}
