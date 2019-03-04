package fun.hercules.user.common.exceptions;

import fun.hercules.user.common.errors.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AuthorizationException extends HttpException {

    public AuthorizationException(ErrorCode code, String message) {
        super(code, message);
    }

    public AuthorizationException(ErrorCode code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public AuthorizationException(ErrorCode code) {
        super(code);
    }
}
