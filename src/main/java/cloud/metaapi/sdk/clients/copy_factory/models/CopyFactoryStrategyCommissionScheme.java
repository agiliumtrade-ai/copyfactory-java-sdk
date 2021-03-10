package cloud.metaapi.sdk.clients.copy_factory.models;

/**
 * CopyFactory strategy commission scheme
 */
public class CopyFactoryStrategyCommissionScheme {
    /**
     * Commission type. One of flat-fee, lots-traded, lots-won, amount-traded, amount-won, high-water-mark
     */
    public String type;
    /**
     * Billing period. One of week, month, quarter
     */
    public String billingPeriod;
    /**
     * Commission rate. Should be greater than or equal to zero if commission type is flat-fee, lots-traded or lots-won,
     * should be greater than or equal to zero and less than or equal to 1 if commission type is amount-traded,
     * amount-won, high-water-mark.
     */
    public double commissionRate;
}