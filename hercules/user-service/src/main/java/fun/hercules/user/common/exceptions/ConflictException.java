package fun.hercules.user.common.exceptions;

import fun.hercules.user.common.errors.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends HttpException {
    public ConflictException(ErrorCode code, String message) {
        super(code, message);
    }

    public ConflictException(ErrorCode code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ConflictException(ErrorCode code) {
        super(code);
    }

}
