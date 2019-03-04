package fun.hercules.webapi.security;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public enum ErrorCode {
    INVALID_PASSWORD(31012, "invalid username or password"),
    INVALID_CAPTCHA(31013, "invalid captcha"),
    CAN_NOT_GET_USER_FROM_TOKEN(31014, "can't get user from token"),
    TOKEN_EXPIRED(31015, "token has expired."),
    DISABLED_STATUS(31016, "disabled status."),
    INVALID_ROLE(31017, "invalid user role"),
    INVALID_USER(31018, "invalid user"),
    UNKNOWN_ERROR(31019, "unknown error");

    private static final Set<String> ERROR_CODE_VALUES = ImmutableSet.copyOf(
            Arrays.stream(values()).map(Enum::toString).collect(Collectors.toSet())
    );

    private final int code;

    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static boolean contains(String code) {
        return ERROR_CODE_VALUES.contains(code);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @JsonValue
    public ErrorMessage toErrorMessage() {
        return new ErrorMessage(code, message, new HashMap<>());
    }
}
