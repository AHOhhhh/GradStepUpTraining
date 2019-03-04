package fun.hercules.order.order.platform.order.exceptions;

public class PrimaryKeyException extends RuntimeException {
    public PrimaryKeyException(String message, Throwable ex) {
        super(message, ex);
    }
}
