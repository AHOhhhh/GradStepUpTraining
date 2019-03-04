package fun.hercules.order.order.common.errors;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public enum ErrorCode {
    //common error code
    INVALID_REQUEST(10000, "invalid request"),
    SERVER_ERROR(10001, "server error"),
    ORDER_NOT_FOUND(10002, "order not found"),
    ORDER_TYPE_NOT_FOUND(10003, "order type not found"),
    ORDER_ACCESS_DENIED(10004, "user doesn't have the access privilege to order"),
    INVALID_USER_INFO(10005, "invalid user information"),
    VALIDATION_ERROR(10006, "validation error"),
    BUSINESS_SERVER_ERROR(10008, "downstream business server error"),
    ORDER_CLOSED(10010, "The order has been closed."),
    OPEN_APP_CREDENTIAL_NOT_FOUND(10011, "can't find open app credential to sign business api"),
    UNAUTHORIZED(10012, "not authorized"),
    INVALID_ORDER_ID(10013, "order id is invalid"),
    INVALID_STATUS_TRANSITION(10014, "invalid status transition"),

    // wms错误码
    WMS_OPEN_ORDER_NOT_FOUND(11000, "can't find wms open subscription id for renew and recharge order"),
    CREATE_ORDER_ERROR(11001, "user has created order"),
    REGION_CODE_NOT_FOUND(12004, "region code not found"),

    //航空货运错误码
    ACG_ERROR_CODE_START(13000, "acg error code start"),

    //支付错误码
    PAYMENT_NOT_FOUND(16000, "order payment not found"),
    PAYMENT_REQUEST_NOT_FOUND(16001, "order payment request not found"),
    PAYMENT_DUPLICATED_PAYMENT_REQUEST(16002, "payment requests conflicts"),
    PAYMENT_AMOUNT_CALCULATE_ERROR(16003, "payment amount calculate error"),
    PAYMENT_CONFLICT(16004, "payment existed"),
    INVALID_PAYMENT_PRICE(16005, "invalid payment price"),
    MISSING_ACCOUNT_INFO(16006, "missing account information"),
    MISSING_BANK_TRANSACTION_NUMBER(16007, "missing bank transaction number"),
    MISSING_PAYMENT_TIME(16008, "missing payment time"),
    PAYMENT_INFO_NOT_CORRECT(16009, "payment info not correct"),
    TRANSACTION_NOT_FOUND(16010, "can not find transaction"),

    //账单错误码
    UPLOAD_FILE_SIZE_TOO_LARGE(17000, "upload file size exceeds the limitation"),
    ORDER_BILL_NOT_FOUND(17001, "order bill not found"),
    ORDER_BILL_ALREADY_EXISTED(17002, "order bill already existed"),
    MISSING_FIELD(17003, "missing field when download excel"),
    FORMAT_ERROR(17004, "format not right"),
    ORDER_BILL_NUMBER_EXCEED_ONE(17005, "order bill exceed one"),
    PARSE_ORDER_BILL_STATUS_FAIL(17006, "parse order bill status fail"),

    // Payments
    NEW_PAY_FIELD_ERROR(10003, "new pay entity fields error"),
    CREATE_SIGN_MESSAGE_ERROR(10004, "create sign message error"),
    NOT_SUPPORT_ERROR(10005, "not support other encryption algorithm"),
    MISSING_FIELDS(10006, "some fields don't have valid value"),
    CURRENCY_CODE_NOT_FOUND(10007, "currency code not found"),
    ORG_CODE_NOT_FOUND(10008, "org code not found"),
    PAY_CHANNEL_NOT_FOUND(10009, "pay channel not found"),
    ONLINE_PAY_TRANSACTION_NOT_FOUND(10010, "online pay transaction not found"),
    BYTE_TO_HEX_ERROR(10011, "fail to decode"),
    CONNECT_WITH_NEWPAY_ERROR(10012, "call new pay error"),
    NOT_FOUND(10013, "no resource can be found"),
    QUERY_DETAILS_FROM_NEW_PAY_ERROR(10014, "meet some error when query detail from new pay"),
    NO_AUTHORIZED_ERROR(10015, "unAuthorized"),
    DECODE_ERROR(10016, "fail to decode string"),
    CURRENCY_NOT_RIGHT(10017, "only support CNY payment"),
    MONEY_TOO_LITTLE(10018, "money can not less than 0.01"),
    PAY_TYPE_NOT_RIGHT(10019, "payType not right"),
    ALIAS_NOT_FOUND(10020, "alias not found"),
    OFFLINE_PAY_TRANSACTION_NOT_FOUND(10021, "offline pay transaction not found"),
    COMMENT_NOT_EXISTED(10022, "should have comment if not confirmed");

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
