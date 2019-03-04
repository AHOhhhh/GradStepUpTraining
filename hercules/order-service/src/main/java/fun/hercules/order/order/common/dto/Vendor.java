package fun.hercules.order.order.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vendor {

    private String id;

    private String name;

    private BusinessType businessType;

    private String payMethods;
}
