package cloud.metaapi.sdk.clients.copy_factory.models;

import cloud.metaapi.sdk.clients.models.IsoTime;

/**
 * Trade copying user log record
 */
public class CopyFactoryUserLogRecord {
  
  /**
   * Log level values
   */
  public enum LogLevel { INFO, WARN, ERROR };
  
  /**
   * Log record time
   */
  public IsoTime time;
  /**
   * Log level
   */
  public LogLevel level;
  /**
   * Log message
   */
  public String message;
  /**
   * Symbol traded, or {@code null}
   */
  public String symbol;
  /**
   * Id of the strategy event relates to, or {@code null}
   */
  public String strategyId;
  /**
   * Name of the strategy event relates to, or {@code null}
   */
  public String strategyName;
  /**
   * Position id event relates to, or {@code null}
   */
  public String positionId;
  /**
   * Side of the trade event relates to. One of buy, sell, close, or {@code null}
   */
  public String side;
  /**
   * Type of the trade event relates to. One of market, limit, stop, or {@code null}
   */
  public String type;
  /**
   * Open price for limit and stop orders, or {@code null}
   */
  public Double openPrice;
}