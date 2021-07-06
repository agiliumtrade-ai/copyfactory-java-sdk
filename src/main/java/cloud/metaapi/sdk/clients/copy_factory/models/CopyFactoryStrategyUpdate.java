package cloud.metaapi.sdk.clients.copy_factory.models;

import java.util.List;

/**
 * CopyFactory strategy update
 */
public class CopyFactoryStrategyUpdate {
    /**
     * Strategy human-readable name
     */
    public String name;
    /**
     * Longer strategy human-readable description
     */
    public String description;
    /**
     * Position detection mode. Allowed values are netting (single position per strategy per symbol), 
     * hedging (multiple positions per strategy per symbol)
     */
    public String positionLifecycle;
    /**
     * Id of the MetaApi account providing the strategy
     */
    public String connectionId;
    /**
     * Optional flag indicating that pending orders should not be copied, or {@code null}. 
     * Default is to copy pending orders
     */
    public Boolean skipPendingOrders;
    /**
     * Commission scheme allowed by this strategy, or {@code null}
     */
    public CopyFactoryStrategyCommissionScheme commissionScheme;
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
     * level) or by-symbol (limit correlations on symbol level)
     */
    public String reduceCorrelations;
    /**
     * Optional stop out setting, or {@code null}. All trading will be terminated and positions closed once equity
     * drawdown reaches this value
     */
    public CopyFactoryStrategyStopOutRisk stopOutRisk;
    /**
     * Symbol filters which can be used to copy only specific symbols or exclude some symbols from copying,
     * or {@code null}
     */
    public CopyFactoryStrategySymbolFilter symbolFilter;
    /**
     * News risk filter configuration, or {@code null}
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
     * Optional max leverage risk restriction. All trades resulting in a leverage value higher than specified will be
     * skipped
     */
    public Double maxLeverage;
    /**
     * Optional magic (expert id) filter, or {@code null}
     */
    public CopyFactoryStrategyMagicFilter magicFilter;
    /**
     * Settings to manage copying timeframe and position lifetime, or {@code null}. Default is to copy position within
     * 1 minute from being opened at source and let the position to live for up to 90 days
     */
    public CopyFactoryStrategyTimeSettings timeSettings;
    /**
     * Defines how symbol name should be changed when trading (e.g. when broker uses symbol names with unusual
     * suffixes). By default ({@code null}) this setting is disabled and the trades are copied using signal
     * source symbol name
     */
    public List<CopyFactoryStrategySymbolMapping> symbolMapping;
    /**
     * Trade size scaling settings, or {@code null}. By default the trade size on strategy
     * subscriber side will be scaled according to balance to preserve risk.
     */
    public CopyFactoryStrategyTradeSizeScaling tradeSizeScaling;
    /**
     * Filter which permits the trades only if account equity is greater than balance moving average
     */
    public CopyFactoryStrategyEquityCurveFilter equityCurveFilter;
    /**
     * Flag indicating whether stop loss should be copied. Default ({@code null}) is to copy stop loss
     */
    public Boolean copyStopLoss;
    /**
     * Flag indicating whether take profit should be copied. Default ({@code null}) is to copy take profit
     */
    public Boolean copyTakeProfit;
    /**
     * Minimum trade volume to copy, or {@code null}.
     * Trade signals with a smaller volume will not be copied
     */
    public Double minTradeVolume;
    /**
     * Maximum trade volume to copy, or {@code null}.
     * Trade signals with a larger volume will be copied with maximum volume instead
     */
    public Double maxTradeVolume;
}