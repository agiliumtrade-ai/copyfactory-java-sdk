package cloud.metaapi.sdk.clients.copy_factory.models;

import java.util.List;

/**
 * CopyFactory symbol filter
 */
public class CopyFactoryStrategySymbolFilter {
    /**
     * List of symbols copied, or {@code null}. Leave the value empty to copy all symbols
     */
    public List<String> included;
    /**
     * List of symbols excluded from copying, or {@code null}. Leave the value empty to copy all symbols
     */
    public List<String> excluded;
}