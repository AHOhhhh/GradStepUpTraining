package fun.hercules.order.order.common.signature.openapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppCredential {
    private String name;

    private String description;

    private String appKey;

    private String appSecretKey;

    private Instant expiry;

    private String signVersion;

    private String signAlgorithm;

    private String signScope;

    private String userId;
}
