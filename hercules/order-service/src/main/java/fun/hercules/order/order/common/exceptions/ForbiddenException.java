package fun.hercules.order.order.common.exceptions;

import fun.hercules.order.order.common.errors.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends HttpException {
    public ForbiddenException(ErrorCode code, String message) {
        super(code, message);
    }


    public ForbiddenException(ErrorCode code) {
        super(code);
    }

}
