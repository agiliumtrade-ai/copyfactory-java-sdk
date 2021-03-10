package cloud.metaapi.sdk.clients.copy_factory.models;

import cloud.metaapi.sdk.clients.models.IsoTime;

/**
 * CopyFactory strategy stopout settings
 */
public class CopyFactoryStrategyStopOutRisk {
    /**
     * Value of the stop out risk, expressed as a fraction of 1
     */
    public double value;
    /**
     * The time to start risk calculation from, or {@code null}. All previous trades will be ignored. 
     * You can use it to reset the risk counter after a stopout event
     */
    public IsoTime startTime;
}