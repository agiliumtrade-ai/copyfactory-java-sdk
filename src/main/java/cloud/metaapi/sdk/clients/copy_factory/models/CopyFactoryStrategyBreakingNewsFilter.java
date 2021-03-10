package cloud.metaapi.sdk.clients.copy_factory.models;

import java.util.List;

/**
 * CopyFactory breaking news risk filter
 */
public class CopyFactoryStrategyBreakingNewsFilter {
    /**
     * List of breaking news priorities to stop trading on, or {@code null}. Leave empty to disable breaking news filter.
     * One of high, medium, low.
     */
    public List<String> priorities;
    /**
     * Optional time interval specifying when to force close an already open position before calendar news,
     * or {@code null}. Default value is 60 minutes
     */
    public Integer closePositionTimeGapInMinutes;
    /**
     * Optional time interval specifying when it is allowed to open position after calendar news, or {@code null}.
     * Default value is 60 minutes
     */
    public Integer openPositionFollowingTimeGapInMinutes;
}