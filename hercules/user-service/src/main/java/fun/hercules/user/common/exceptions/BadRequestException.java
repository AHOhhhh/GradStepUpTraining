package fun.hercules.user.common.exceptions;

import fun.hercules.user.common.errors.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends HttpException {
    public BadRequestException(ErrorCode code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public BadRequestException(ErrorCode code) {
        super(code, code.getMessage());
    }

    public BadRequestException(ErrorCode code, String message) {
        super(code, message);
    }
}
