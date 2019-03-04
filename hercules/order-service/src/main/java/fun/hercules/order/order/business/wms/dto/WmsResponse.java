package fun.hercules.order.order.business.wms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WmsResponse {
    String status;

    String msg;

    public enum StatusCode {
        SUCCESS("0");

        @Getter
        private final String code;

        StatusCode(String code) {
            this.code = code;
        }
    }

}
