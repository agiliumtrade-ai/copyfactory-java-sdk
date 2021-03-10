package cloud.metaapi.sdk.clients.copy_factory.models;

/**
 * CopyFactory strategy equity curve filter
 */
public class CopyFactoryStrategyEquityCurveFilter {
  /**
   * Moving average period, must be greater or equal to 1
   */
  public float period;
  /**
   * Moving average granularity, a positive integer followed by time unit, e.g. 2h.
   * Allowed units are s, m, h, d and w.
   */
  public String granularity;
}
