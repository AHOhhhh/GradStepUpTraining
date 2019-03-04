package fun.hercules.order.order.common.exceptions;

import fun.hercules.order.order.common.errors.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends HttpException {
    public UnauthorizedException(ErrorCode code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public UnauthorizedException(ErrorCode code) {
        super(code);
    }

    public UnauthorizedException(ErrorCode code, String message) {
        super(code, message);
    }
}
