package cloud.metaapi.sdk.clients.copy_factory.models;

import java.util.List;

/**
 * CopyFactory strategy magic filter
 */
public class CopyFactoryStrategyMagicFilter {
    /**
     * List of magics (expert ids) or magic ranges copied, or {@code null}. Leave the value empty to copy all magics
     */
    public List<String> included;
    /**
     * List of magics (expert ids) or magic ranges excluded from copying, or {@code null}. Leave the value empty to
     * copy all magics
     */
    public List<String> excluded;
}