package cloud.metaapi.sdk.clients.error_handler;

/**
 * Throwing this error results in 401 (Unauthorized) HTTP response code.
 */
public class UnauthorizedException extends ApiException {

    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs unauthorized error.
     * @param message error message
     */
    public UnauthorizedException(String message) {
        super(message, 401);
    }
}