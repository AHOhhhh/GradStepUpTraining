package fun.hercules.order.order.platform.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

@Data
@Builder
public class UploadExcelResponse {
    private int count;
    private List<String> duplicatedOrderIds;
    private List<String> nonExistOrderIds;
    private List<String> statusErrorOrderIds;

    @JsonIgnore
    public boolean isSucceed() {
        return CollectionUtils.isEmpty(duplicatedOrderIds)
                && CollectionUtils.isEmpty(nonExistOrderIds)
                && CollectionUtils.isEmpty(statusErrorOrderIds);
    }

    public String getStatus() {
        return isSucceed() ? "success" : "failed";
    }
}
