package fun.hercules.order.order.common.interceptor;

import fun.hercules.order.order.common.errors.ErrorCode;
import fun.hercules.order.order.common.errors.ErrorMessage;
import fun.hercules.order.order.common.exceptions.HttpException;
import fun.hercules.order.order.common.exceptions.PayloadTooLargeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fun.hercules.order.order.common.errors.ErrorCode.VALIDATION_ERROR;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage handleHttpClientError(MethodArgumentNotValidException ex) {
        // TODO: 07/11/2017 check if we need to make the error response so detailed.
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        if (errors.size() == 1 && ErrorCode.contains(errors.get(0).getCode())) {
            ErrorCode errorCode = ErrorCode.valueOf(errors.get(0).getCode());
            log.warn("invalid request with error code: {}", errorCode);
            return errorCode.toErrorMessage();
        } else {
            log.warn("invalid request with errors: {}", errors);
            return ErrorCode.INVALID_REQUEST.toErrorMessage().withDetails("errors", errors);
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage handleHttpClientError(IllegalArgumentException ex) {
        log.warn("invalid request", ex);
        return new ErrorMessage(ErrorCode.INVALID_REQUEST.getCode(), ex.getMessage());
    }

    @ExceptionHandler(HttpException.class)
    @ResponseBody
    public ResponseEntity handleHttpException(HttpException ex) {
        ResponseStatus responseStatus = ex.getClass().getDeclaredAnnotation(ResponseStatus.class);
        log.warn(String.format("http exception code %s", responseStatus), ex);
        return ResponseEntity.status(responseStatus.value())
                .body(new ErrorMessage(ex.getErrorCode().getCode(), ex.getMessage()));
    }

    @ExceptionHandler(TransactionSystemException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleValidationException(TransactionSystemException ex) {
        Throwable cause = ex.getMostSpecificCause();
        if (cause instanceof ConstraintViolationException) {
            Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) cause).getConstraintViolations();
            List<String> errors = violations.stream()
                    .map(constraintViolation -> String.format(
                            "%s:%s",
                            constraintViolation.getPropertyPath(),
                            constraintViolation.getMessage()))
                    .collect(Collectors.toList());

            if (errors.size() >= 1) {
                log.info("validation errors: {}", errors);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorMessage(VALIDATION_ERROR.getCode(), errors.get(0))
                                .withDetails("errors", errors));
            }
        }
        log.warn("transaction system exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage(ErrorCode.SERVER_ERROR.getCode(), cause.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorMessage handleAccessDeniedException(AccessDeniedException ex) {
        return ErrorCode.ORDER_ACCESS_DENIED.toErrorMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorMessage handleException(Exception ex) {
        log.warn("request exception", ex);
        return new ErrorMessage(ErrorCode.SERVER_ERROR.getCode(), ex.getMessage());
    }

    @ExceptionHandler(PayloadTooLargeException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    @ResponseBody
    public ErrorMessage handleException(PayloadTooLargeException ex) {
        return new ErrorMessage(ex.getErrorCode().getCode(), ex.getMessage()).withDetails("permitted", ex.getPermitted());
    }
}
