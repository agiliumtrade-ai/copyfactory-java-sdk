package cloud.metaapi.sdk.clients.error_handler;

/**
 * Throwing this error results in 403 (Forbidden) HTTP response code.
 */
public class ForbiddenException extends ApiException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs forbidden error.
     * @param message error message
     */
    public ForbiddenException(String message) {
        super(message, 403);
    }
}