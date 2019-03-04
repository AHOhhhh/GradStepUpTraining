package fun.hercules.order.order.business.wms.exception;

public class UpdateOrderStatusNotFromPaidException extends RuntimeException {
    public UpdateOrderStatusNotFromPaidException(String message, Throwable ex) {
        super(message, ex);
    }

    public UpdateOrderStatusNotFromPaidException(String message) {
        super(message);
    }
}
