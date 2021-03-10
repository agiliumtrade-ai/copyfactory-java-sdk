package cloud.metaapi.sdk.clients.copy_factory.models;

import java.util.List;

/**
 * CopyFactory provider or subscriber
 */
public class CopyFactorySubscriberOrProvider {
    /**
     * Profile id
     */
    public String id;
    /**
     * User name
     */
    public String name;
    /**
     * List of strategy IDs provided by provider or subscribed to by subscriber
     */
    public List<CopyFactoryStrategyIdAndName> strategies;
}