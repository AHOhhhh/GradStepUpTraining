package fun.hercules.order.order.common.exceptions;

import fun.hercules.order.order.common.errors.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerError extends HttpException {

    public InternalServerError(ErrorCode code, String message) {
        super(code, message);
    }

    public InternalServerError(ErrorCode code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public InternalServerError(ErrorCode code) {
        super(code);
    }
}
