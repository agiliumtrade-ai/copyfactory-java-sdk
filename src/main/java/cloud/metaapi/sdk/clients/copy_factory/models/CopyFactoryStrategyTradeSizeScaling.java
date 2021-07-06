package cloud.metaapi.sdk.clients.copy_factory.models;

/**
 * CopyFactory strategy trade size scaling settings
 */
public class CopyFactoryStrategyTradeSizeScaling {
  /**
   * If set to balance, the trade size on strategy subscriber will be scaled according to
   * balance to preserve risk. If value is none, then trade size will be preserved irregardless of the subscriber
   * balance. If value is contractSize, then trade size will be scaled according to contract size. If fixedVolume is
   * set, then trade will be copied with a fixed volume of traceVolume setting. If fixedRisk is set, then each trade
   * will be copied with a trade volume set to risk specific fraction of balance as configured by riskFraction setting.
   * Note, that in fixedRisk mode trades without a SL are not copied. Default is balance. Allowed values: none,
   * contractSize, balance, fixedVolume, fixedRisk
   */
  public String mode;
  /**
   * Fixed trade volume for use with fixedVolume trade size scaling mode, or {@code null}
   */
  public Double tradeVolume;
  /**
   * Fixed risk fraction for use with fixedRisk trade size scaling mode, or {@code null}
   */
  public Double riskFraction;
}
