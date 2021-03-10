package cloud.metaapi.sdk.clients.copy_factory.models;

/**
 * Trade copying metrics such as slippage and latencies
 */
public class CopyFactoryTransactionMetrics {
    /**
     * Trade copying latency, measured in milliseconds based on transaction time
     * provided by broker, or {@code null}
     */
    public Double tradeCopyingLatency;
    /**
     * Trade copying slippage, measured in basis points (0.01 percent)
     * based on transaction price provided by broker, or {@code null}
     */
    public Double tradeCopyingSlippageInBasisPoints;
    /**
     * Trade copying slippage, measured in account currency
     * based on transaction price provided by broker, or {@code null}
     */
    public Double tradeCopyingSlippageInAccountCurrency;
    /**
     * Trade signal latency introduced by broker and MT platform, measured
     * in milliseconds, or {@code null}
     */
    public Double mtAndBrokerSignalLatency;
    /**
     * Trade algorithm latency introduced by CopyFactory servers, measured in
     * milliseconds, or {@code null}
     */
    public Double tradeAlgorithmLatency;
    /**
     * Trade latency for a copied trade introduced by broker and MT platform,
     * measured in milliseconds, or {@code null}
     */
    public Double mtAndBrokerTradeLatency;
    /**
     * Total trade copying latency, measured in milliseconds, or {@code null}. This value might be
     * slightly different from tradeCopyingLatency value due to limited measurement precision as it is measured based
     * on timestamps captured during copy trading process as opposed to broker data
     */
    public Double totalLatency;
}