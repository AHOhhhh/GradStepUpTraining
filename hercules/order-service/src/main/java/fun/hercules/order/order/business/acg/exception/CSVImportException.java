package fun.hercules.order.order.business.acg.exception;

public class CSVImportException extends RuntimeException {
    public CSVImportException(String message, Throwable ex) {
        super(message, ex);
    }
}
