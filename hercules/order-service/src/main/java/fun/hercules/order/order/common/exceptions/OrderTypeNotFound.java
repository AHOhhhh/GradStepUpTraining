package fun.hercules.order.order.common.exceptions;

import fun.hercules.order.order.common.errors.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderTypeNotFound extends HttpException {
    public OrderTypeNotFound(ErrorCode code) {
        super(code);
    }

    public OrderTypeNotFound(ErrorCode code, String message) {
        super(code, message);
    }
}
