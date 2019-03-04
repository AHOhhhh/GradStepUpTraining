package fun.hercules.user.common.constants;

import java.util.Arrays;

public enum GrantType {
    AUTHORIZATION_CODE("authorization_code"), REFRESH_TOKEN("refresh_token");

    private String value;

    GrantType(String value) {
        this.value = value;
    }

    public static GrantType fromValue(String value) {
        return Arrays.stream(GrantType.values())
                .filter(grantType -> grantType.getValue().equals(value)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("No enum constant with value=%s", value)));
    }

    public String getValue() {
        return value;
    }
}
