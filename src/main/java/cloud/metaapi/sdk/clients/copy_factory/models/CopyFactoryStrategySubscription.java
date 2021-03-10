package cloud.metaapi.sdk.clients.copy_factory.models;

import java.util.List;

/**
 * CopyFactory strategy subscriptions
 */
public class CopyFactoryStrategySubscription {
    /**
     * Id of the strategy to subscribe to
     */
    public String strategyId;
    /**
     * Optional subscription multiplier or {@code null}, default is 1x
     */
    public Double multiplier;
    /**
     * Optional flag indicating that pending orders should not be copied, or {@code null}. 
     * Default is to copy pending orders
     */
    public Boolean skipPendingOrders;
    /**
     * Optional setting wich instructs the application not to open new positions, or {@code null}. by-symbol
     * means that it is still allowed to open new positions with a symbol equal to the symbol of an existing
     * strategy position (can be used to gracefuly exit strategies trading in netting mode or placing a series
     * of related trades per symbol). One of by-position, by-symbol or leave empty to disable this setting.
     */
    public String closeOnly;
    /**
     * Optional max risk per trade, expressed as a fraction of 1, or {@code null}. If trade has a SL, the trade size
     * will be adjusted to match the risk limit. If not, the trade SL will be applied according to the risk limit
     */
    public Double maxTradeRisk;
    /**
     * Flag indicating that the strategy should be copied in a reverse direction, or {@code null}
     */
    public Boolean reverse;
    /**
     * Optional setting indicating whether to enable automatic trade correlation reduction, or {@code null}. Possible
     * settings are not specified (disable correlation risk restrictions), by-strategy (limit correlations on strategy
     * level) or by-symbol (limit correlations on symbol level).
     */
    public String reduceCorrelations;
    /**
     * Optional stop out setting, or {@code null}. All trading will be terminated and positions closed once equity
     * drawdown reaches this value
     */
    public CopyFactoryStrategyStopOutRisk stopOutRisk;
    /**
     * Optional symbol filter, or {@code null}. Can be used to copy only specific symbols or exclude some symbols
     * from copying
     */
    public CopyFactoryStrategySymbolFilter symbolFilter;
    /**
     * Optional news risk filter configuration, or {@code null}
     */
    public CopyFactoryStrategyNewsFilter newsFilter;
    /**
     * Optional strategy risk limits, or {@code null}. You can configure trading to be stopped once total drawdown
     * generated during specific period is exceeded. Can be specified either for balance or equity drawdown
     */
    public List<CopyFactoryStrategyRiskLimit> riskLimits;
    /**
     * Optional stop loss value restriction, or {@code null}
     */
    public CopyFactoryStrategyMaxStopLoss maxStopLoss;
    /**
     * Optional setting indicating maximum leverage allowed when opening a new positions, or {@code null}.
     * Any trade which results in a higher leverage will be discarded
     */
    public Double maxLeverage;
    /**
     * Defines how symbol name should be changed when trading (e.g. when broker uses symbol names with unusual
     * suffixes). By default this setting is disabled ({@code null}) and the trades are copied using signal
     * source symbol name
     */
    public List<CopyFactoryStrategySymbolMapping> symbolMapping;
    /**
     * If set to balance, the trade size on strategy subscriber will be scaled according to balance to preserve
     * risk. If value is none, than trade size will be preserved irregardless of the subscriber balance. If value
     * is contractSize, then trade size will be scaled according to contract size. Default ({@code null}) is balance.
     */
    public String tradeSizeScalingMode;
}