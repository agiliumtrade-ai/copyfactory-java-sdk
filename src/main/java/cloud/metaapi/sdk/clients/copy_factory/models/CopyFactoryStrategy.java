package cloud.metaapi.sdk.clients.copy_factory.models;

/**
 * CopyFactory provider strategy
 */
public class CopyFactoryStrategy extends CopyFactoryStrategyUpdate {
    /**
     * Unique strategy id
     */
    public String _id;
    /**
     * Commission rate the platform charges for strategy copying, applied to commissions charged by provider.
     * This commission applies only to accounts not managed directly by provider. Should be fraction of 1
     */
    public double platformCommissionRate;
}