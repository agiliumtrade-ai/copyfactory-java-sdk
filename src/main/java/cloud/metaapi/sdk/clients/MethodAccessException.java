package cloud.metaapi.sdk.clients;

/**
 * Error which indicates that user doesn't have access to a method
 */
public class MethodAccessException extends Exception {

    private static final long serialVersionUID = 1L;
    private String message;
    
    /**
     * Constructs the exception. Access type is set to {@code api}
     * @param methodName Name of method
     */
    public MethodAccessException(String methodName) {
        this(methodName, "api");
    }
    
    /**
     * Constructs the exception
     * @param methodName Name of method
     * @param accessType Type of method access
     */
    public MethodAccessException(String methodName, String accessType) {
        super();
        switch (accessType) {
        case "api": 
            this.message = "You can not invoke " + methodName + " method, because you have connected with API access"
                + " token. Please use account access token to invoke this method.";
            break;
        case "account":
            this.message = "You can not invoke " + methodName + " method, because you have connected with account access"
                + " token. Please use API access token from https://app.metaapi.cloud/token page to invoke this method.";
            break;
        default:
            this.message = "";
            break;
        }
    }
    
    @Override
    public String getMessage() {
        return message;
    }
}