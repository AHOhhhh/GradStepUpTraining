package fun.hercules.user.support;


import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class PayMethodConfigItem {
    private String orderType;

    private List<String> editable;

    private List<String> defaults;
}
