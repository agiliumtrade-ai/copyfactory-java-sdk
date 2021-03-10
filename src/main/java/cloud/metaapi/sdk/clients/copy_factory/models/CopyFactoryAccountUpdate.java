package cloud.metaapi.sdk.clients.copy_factory.models;

import java.util.List;

/**
 * CopyFactory account update
 */
public class CopyFactoryAccountUpdate {
    /**
     * Account human-readable name
     */
    public String name;
    /**
     * Id of the MetaApi MetaTrader account this copy trading account is connected to
     */
    public String connectionId;
    /**
     * Optional fraction of reserved margin to reduce a risk of margin call, or {@code null}. Default is to reserve 
     * no margin. We recommend using maxLeverage setting instead. Specified as a fraction of balance thus the value
     * is usually greater than 1
     */
    public Double reservedMarginFraction;
    /**
     * Optional phone numbers to send sms notifications to, or {@code null}. Leave empty to receive no sms notifications
     */
    public List<String> phoneNumbers;
    /**
     * Optional value of minimal trade size allowed, expressed in amount of account currency, or {@code null}. 
     * Can be useful if your broker charges a fixed fee per transaction so that you can skip small trades with
     * high broker commission rates. Default is 100
     */
    public Double minTradeAmount;
    /**
     * Optional setting which instructs the application not to open new positions. by-symbol means that it is still 
     * allowed to open new positions with a symbol equal to the symbol of an existing strategy position (can be used
     * to gracefuly exit strategies trading in netting mode or placing a series of related trades per symbol). 
     * One of by-position, by-symbol or leave empty to disable this setting.
     */
    public String closeOnly;
    /**
     * Pptional stop out setting, or {@code null}. All trading will be terminated and positions closed once equity
     * drawdown reaches this value
     */
    public CopyFactoryStrategyStopOutRisk stopOutRisk;
    /**
     * Optional account risk limits, or {@code null}. You can configure trading to be stopped once total drawdown
     * generated during specific period is exceeded. Can be specified either for balance or equity drawdown
     */
    public List<CopyFactoryStrategyRiskLimit> riskLimits;
    /**
     * Optional setting indicating maxumum leverage allowed when opening a new positions, or {@code null}.
     * Any trade which results in a higher leverage will be discarded.
     */
    public Double maxLeverage;
    /**
     * Strategy subscriptions
     */
    public List<CopyFactoryStrategySubscription> subscriptions;
}