package cloud.metaapi.sdk.clients.error_handler;

/**
 * Throwing this error results in 404 (Not Found) HTTP response code.
 */
public class NotFoundException extends ApiException {

    private static final long serialVersionUID = 1L;

    /**
     * Represents NotFoundError.
     * @param message error message
     */
    public NotFoundException(String message) {
        super(message, 404);
    }
}