package cloud.metaapi.sdk.clients.error_handler;

import java.util.List;

/**
 * Base class for API errors. Contains indication of HTTP status.
 */
public class ApiException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * HTTP status code
     */
    public int status;
    
    private String code;
    private List<Object> args;

    /**
     * ApiError constructor
     * @param message error message
     * @param status HTTP status
     */
    public ApiException(String message, int status) {
        this(message, status, null);
    }
    
    /**
     * ApiError constructor
     * @param message error message
     * @param status HTTP status
     * @param cause the cause
     */
    public ApiException(String message, int status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    /**
     * Sets error code, used for i18n
     * @param code error code for i18n
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Returns error code used for i18n
     * @return error code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Set message arguments for i18n
     * @param args arguments for i18n
     */
    public void setArguments(List<Object> args) {
        this.args = args;
    }

    /**
     * Returns message arguments for i18n
     * @return message arguments for i18n
     */
    public List<Object> getArguments() {
        return this.args;
    }
}