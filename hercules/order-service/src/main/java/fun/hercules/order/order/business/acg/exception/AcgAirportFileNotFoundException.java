package fun.hercules.order.order.business.acg.exception;

public class AcgAirportFileNotFoundException extends RuntimeException {
    public AcgAirportFileNotFoundException(String message, Throwable ex) {
        super(message, ex);
    }
}
