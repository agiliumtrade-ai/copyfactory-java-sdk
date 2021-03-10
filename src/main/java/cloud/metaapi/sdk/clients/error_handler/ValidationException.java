package cloud.metaapi.sdk.clients.error_handler;

/**
 * Represents validation error. Throwing this error results in 400 (Bad Request) HTTP response code.
 */
public class ValidationException extends ApiException {

    private static final long serialVersionUID = 1L;
    
    /**
     * Validation error details
     */
    public Object details;

    /**
     * Constructs validation error.
     * @param message error message
     * @param details error data
     */
    public ValidationException(String message, Object details) {
        super(message, 400);
        this.details = details;
    }
}