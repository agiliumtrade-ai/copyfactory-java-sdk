package cloud.metaapi.sdk.clients.copy_factory.models;

/**
 * CopyFactory news risk filter
 */
public class CopyFactoryStrategyNewsFilter {
    /**
     * Optional breaking news filter, or {@code null}
     */
    public CopyFactoryStrategyBreakingNewsFilter breakingNewsFilter;
    /**
     * Optional calendar news filter, or {@code null}
     */
    public CopyFactoryStrategyCalendarNewsFilter calendarNewsFilter;
}