package cloud.metaapi.sdk.clients.copy_factory.models;

/**
 * CopyFactory strategy time settings
 */
public class CopyFactoryStrategyTimeSettings {
    /**
     * Optional position lifetime, or {@code null}. Default is to keep positions open up to 90 days
     */
    public Integer lifetimeInHours;
    /**
     * Optional time interval to copy new positions, or {@code null}. Default is to let 1 minute for the position to get
     * copied. If position were not copied during this time, the copying will not be retried anymore.
     */
    public Integer openingIntervalInMinutes;
}