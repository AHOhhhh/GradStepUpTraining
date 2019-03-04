package fun.hercules.order.order.common.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseBase {

    @JsonProperty(value = "status_code")
    protected String statusCode;

    protected String message;

    public enum StatusCode {
        SUCCESS("0");

        @Getter
        private final String code;

        StatusCode(String code) {
            this.code = code;
        }
    }

    public static ResponseBase success() {
        return new ResponseBase("0", "success");
    }

    public boolean isSuccess() {
        return statusCode.equals("0");
    }
}
