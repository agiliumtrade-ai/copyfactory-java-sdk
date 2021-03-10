package cloud.metaapi.sdk.clients.copy_factory.models;

import cloud.metaapi.sdk.clients.models.IsoTime;

/**
 * CopyFactory strategy stopout
 */
public class CopyFactoryStrategyStopout {
    /**
     * Strategy which was stopped out
     */
    public CopyFactoryStrategyIdAndName strategy;
    /**
     * Stopout reason. One of yearly-balance, monthly-balance, daily-balance, yearly-equity, monthly-equity,
     * daily-equity, max-drawdown
     */
    public String reason;
    /**
     * Human-readable description of the stopout reason
     */
    public String reasonDescription;
    /**
     * Time the strategy was stopped at
     */
    public IsoTime stoppedAt;
    /**
     * Time the strategy is stopped till
     */
    public IsoTime stoppedTill;
}