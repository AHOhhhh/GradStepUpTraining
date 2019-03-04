package fun.hercules.user.common.interceptor;

import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.common.errors.ErrorMessage;
import fun.hercules.user.common.exceptions.BadRequestException;
import fun.hercules.user.common.exceptions.HttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Global exception processor
 * 对全文所有controller抛出的异常，且自身controller没做处理的异常，做统一处理，例如400、401、403等异常
 */
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
            log.info("invalid request with error code: {}", errorCode);
            return errorCode.toErrorMessage();
        } else {
            log.info("invalid request with errors: {}", errors);
            return ErrorCode.INVALID_REQUEST.toErrorMessage().withDetails("errors", errors);
        }
    }

    /**
     * 权限不足异常，对应HTTP 403异常
     *
     * @param ex 访问拒绝异常
     * @return 错误信息
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorMessage handleAccessDeniedException(AccessDeniedException ex) {
        return ErrorCode.FORBIDDEN.toErrorMessage();
    }

    @ExceptionHandler(HttpException.class)
    @ResponseBody
    public ResponseEntity handleHttpException(HttpException ex) {
        ResponseStatus responseStatus = ex.getClass().getDeclaredAnnotation(ResponseStatus.class);
        return ResponseEntity.status(responseStatus.value()).body(new ErrorMessage(ex.getErrorCode().getCode(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage(ErrorCode.SERVER_ERROR.getCode(), ex.getMessage()));
    }

    /**
     * 对应400异常
     *
     * @param ex 请求无效异常
     * @return 错误码和错误信息
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public ResponseEntity handleBadRequestException(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(ex.getErrorCode().getCode(), ex.getErrorCode().getMessage()));
    }
}
