package cloud.metaapi.sdk.clients.models;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Contains an error message
 */
public class Error {
    /**
     * Error id
     */
    public int id;
    /**
     * Error name
     */
    public String error;
    /**
     * Numeric error code or {@code null}
     */
    public Integer numericCode = null;
    /**
     * String error code or {@code null}
     */
    public String stringCode = null;
    /**
     * Error description
     */
    public String message;
    /**
     * Additional information about error or {@code null}. Used to supply validation error details
     */
    public JsonNode details = null;
    /**
     * Error metadata or {@code null}
     */
    public JsonNode metadata = null;
}
