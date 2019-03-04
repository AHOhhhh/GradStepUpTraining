package fun.hercules.user.common.errors;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public enum ErrorCode {
    INVALID_REQUEST(10000, "invalid request"),
    SERVER_ERROR(10001, "server error"),
    PASSWORD_LENGTH_TOO_SHORT(30001, "password length must longer than 8"),
    WEAK_PASSWORD(30002, "password doesn't meet requirements"),
    MISSING_PHONE_NUMBER(30003, "missing phone number"),
    INVALID_UPLOAD_FILE(30004, "the file should not bigger than 3M and the type should be png or jpg or jpeg"),
    FAILED_TO_CREATE_ENTERPRISE(30005, "failed to create enterprise "),
    USER_ALREADY_EXISTS(30006, "user already exists"),
    FAILED_TO_CREATE_USER(30007, "failed to create user"),
    USER_NOT_FOUND(30008, "user not found"),
    FAILED_TO_STORE_FILE(30009, "failed to store file"),
    FILE_NOT_FOUND(30010, "file not found"),
    ROLE_NOT_FOUND(30011, "role not found"),
    INVALID_PASSWORD(30012, "invalid password"),
    INVALID_CAPTCHA(30013, "invalid captcha"),
    INVALID_CAPTCHA_ID(30014, "Invalid ID, could not validate unexisting or already validated captcha"),
    UPLOAD_FILE_SIZE_TOO_LARGE(30015, "upload file size exceeds the limitation"),
    TOKEN_EXPIRED(30016, "token has expired."),
    ENTERPRISE_NOT_FOUND(30017, "enterprise not found"),
    FAILED_TO_RESIZED_IMAGE(30018, "failed to resized picture"),
    MISSING_FULL_NAME(30019, "missing full name"),
    MISSING_EMAIL(30020, "missing email"),
    NO_AUTHORIZATION(30021, "no authorization"),
    FORBIDDEN(30022, "not allowed"),
    CONTACT_ALREADY_EXISTS(30023, "contact already exists"),
    CONTACT_NOT_FOUND(30024, "contact not found"),
    APP_KEY_NOT_FOUND(30025, "invalid app key"),
    STATUS_NOT_FOUND(30026, "status not found"),
    WRONG_GRANT_TYPE(30027, "wrong enum value"),
    UNAUTHORIZED(30028, "not authorized"),
    INVALID_PARAMETER(30029, "Invalid parameter"),
    INVALID_ENUM_VALUE(30030, "invalid enum value"),
    CONTACT_PHONE_NOT_PROVIDED(30031, "should provide at least one phone number"),
    APP_CREDENTIAL_ALREADY_EXISTS(30006, "user already exists"),
    MISSING_ENTERPRISE_ID(30032, "missing enterprise_id"),
    SSO_FILE_TYPE_NOT_FOUND(3033, "invalid sso file type"),
    PAY_METHOD_NOT_FOUND(3034, "payMethod not found"),
    MISSING_PRIVILEGE(30035, "missing privilege"),
    PRIVILEGE_NOT_FOUND(30036, "invalid privilege");

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
