package fun.hercules.order.payment.transaction.exception;

public class PrimaryKeyException extends RuntimeException {
    public PrimaryKeyException(String message, Throwable ex) {
        super(message, ex);
    }
}
