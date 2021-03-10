package cloud.metaapi.sdk.clients.error_handler;

import cloud.metaapi.sdk.clients.models.IsoTime;

/**
 * Represents too many requests error. Throwing this error results in 429 (Too Many Requests) HTTP response code.
 */
public class TooManyRequestsException extends ApiException {

  private static final long serialVersionUID = 1L;
  
  /**
   * Exception metadata
   */
  public static class TooManyRequestsExceptionMetadata {
    /**
     * Throttling period in minutes
     */
    public int periodInMinutes;
    /**
     * Available requests for periodInMinutes
     */
    public int requestsPerPeriodAllowed;
    /**
     * Recommended date to retry request
     */
    public IsoTime recommendedRetryTime;
  }
  
  public TooManyRequestsExceptionMetadata metadata;
  
  /**
   * Constructs too many requests error.
   * @param message error message
   * @param metadata error metadata, or {@code null}
   */
  public TooManyRequestsException(String message, TooManyRequestsExceptionMetadata metadata) {
    super(message, 429);
    this.metadata = metadata;
  }
}