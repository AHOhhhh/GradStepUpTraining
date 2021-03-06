package fun.hercules.order.order.common.exceptions;

import fun.hercules.order.order.common.errors.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends HttpException {

    public InternalServerException(ErrorCode code, String message) {
        super(code, message);
    }

    public InternalServerException(ErrorCode code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public InternalServerException(ErrorCode code) {
        super(code);
    }
}
