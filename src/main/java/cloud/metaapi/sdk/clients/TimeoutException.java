package cloud.metaapi.sdk.clients;

/**
 * Error which indicates a timeout
 */
public class TimeoutException extends Exception {

    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs the timeout error
     * @param message error message
     */
    public TimeoutException(String message) {
        super(message);
    }
}