package cloud.metaapi.sdk.clients.copy_factory.models;

/**
 * CopyFactory strategy max stop loss settings
 */
public class CopyFactoryStrategyMaxStopLoss {
    /**
     * Maximum SL value
     */
    public double value;
    /**
     * SL units. Only pips value is supported at this point
     */
    public String units;
}