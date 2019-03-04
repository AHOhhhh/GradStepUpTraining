package fun.hercules.order.order.common.exceptions;

import lombok.Getter;

import java.text.MessageFormat;

import static fun.hercules.order.order.common.errors.ErrorCode.UPLOAD_FILE_SIZE_TOO_LARGE;

@Getter
public class PayloadTooLargeException extends HttpException {
    private long permitted;

    public PayloadTooLargeException(long actual,
                                    long permitted) {
        super(UPLOAD_FILE_SIZE_TOO_LARGE,
                MessageFormat.format("Payload too large. permitted is {1} but actual is {0}", actual, permitted));
        this.permitted = permitted;
    }
}
