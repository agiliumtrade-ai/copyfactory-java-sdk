package cloud.metaapi.sdk.clients.copy_factory.models;

import cloud.metaapi.sdk.clients.models.IsoTime;

/**
 * CopyFactory transaction
 */
public class CopyFactoryTransaction {
  
  /**
   * Deal type. See https://www.mql5.com/en/docs/constants/tradingconstants/dealproperties#enum_deal_type
   */
  public enum DealType {
    DEAL_TYPE_BUY, DEAL_TYPE_SELL, DEAL_TYPE_BALANCE, DEAL_TYPE_CREDIT, DEAL_TYPE_CHARGE,
    DEAL_TYPE_CORRECTION, DEAL_TYPE_BONUS, DEAL_TYPE_COMMISSION, DEAL_TYPE_COMMISSION_DAILY, 
    DEAL_TYPE_COMMISSION_MONTHLY, DEAL_TYPE_COMMISSION_AGENT_DAILY, DEAL_TYPE_COMMISSION_AGENT_MONTHLY, 
    DEAL_TYPE_INTEREST, DEAL_TYPE_BUY_CANCELED, DEAL_TYPE_SELL_CANCELED, DEAL_DIVIDEND, DEAL_DIVIDEND_FRANKED,
    DEAL_TAX
  }
  
  /**
   * Transaction id
   */
  public String id;
  /**
   * Transaction type
   */
  public DealType type;
  /**
   * Transaction time
   */
  public IsoTime time;
  /**
   * CopyFactory account id
   */
  public String accountId;
  /**
   * Optional symbol traded, or {@code null}
   */
  public String symbol;
  /**
   * Strategy subscriber
   */
  public CopyFactorySubscriberOrProvider subscriber;
  /**
   * Demo account flag
   */
  public boolean demo;
  /**
   * Strategy provider
   */
  public CopyFactorySubscriberOrProvider provider;
  /**
   * Strategy
   */
  public CopyFactoryStrategyIdAndName strategy;
  /**
   * Source position id, or {@code null}
   */
  public String positionId;
  /**
   * High-water mark strategy balance improvement
   */
  public double improvement;
  /**
   * Provider commission
   */
  public double providerCommission;
  /**
   * Platform commission
   */
  public double platformCommission;
  /**
   * Trade volume, or {@code null}
   */
  public Double quantity;
  /**
   * Commission paid by provider to underlying providers, or {@code null}
   */
  public Double incomingProviderCommission;
  /**
   * Platform commission paid by provider to underlying providers, or {@code null}
   */
  public Double incomingPlatformCommission;
  /**
   * Trade lot price, or {@code null}
   */
  public Double lotPrice;
  /**
   * Trade tick price, or {@code null}
   */
  public Double tickPrice;
  /**
   * Trade amount, or {@code null}
   */
  public Double amount;
  /**
   * Trade commission or {@code null}
   */
  public Double commission;
  /**
   * Trade swap
   */
  public Double swap;
  /**
   * Trade profit
   */
  public Double profit;
  /**
   * Trade copying metrics such as slippage and latencies, or {@code null}. Measured selectively for copied trades
   */
  public CopyFactoryTransactionMetrics metrics;
}