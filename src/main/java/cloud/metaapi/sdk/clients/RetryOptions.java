package cloud.metaapi.sdk.clients;

/**
 * Retrying options
 */
public class RetryOptions {
  /**
   * Amount of retries
   */
  public int retries = 5;
  /**
   * Minimum delay in seconds
   */
  public int minDelayInSeconds = 1;
  /**
   * Maximum delay in deconds
   */
  public int maxDelayInSeconds = 30;
}