package cloud.metaapi.sdk.clients.copy_factory.models;

import cloud.metaapi.sdk.clients.models.IsoTime;

/**
 * CopyFactory risk limit filter
 */
public class CopyFactoryStrategyRiskLimit {
    /**
     * Restriction type. One of daily, monthly, or yearly
     */
    public String type;
    /**
     * Account metric to apply limit to. One of balance, equity
     */
    public String applyTo;
    /**
     * Max drawdown allowed, expressed as a fraction of 1
     */
    public double maxRisk;
    /**
     * Whether to force close positions when the risk is reached. If value is false then only the new trades will be 
     * halted, but existing ones will not be closed
     */
    public boolean closePositions;
    /**
     * Optional time to start risk tracking from, or {code null}. All previous trades will be ignored.
     * You can use this value to reset the filter after stopout event
     */
    public IsoTime startTime;
}