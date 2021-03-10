package cloud.metaapi.sdk.clients.error_handler;

/**
 * Represents unexpected error. Throwing this error results in 500 (Internal Error) HTTP response code.
 */
public class InternalException extends ApiException {
    
    private static final long serialVersionUID = 1L;

    /**
     * Constructs unexpected error.
     * @param message error message
     */
    public InternalException(String message) {
        super(message, 500);
    }
}