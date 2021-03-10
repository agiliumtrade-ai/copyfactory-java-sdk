package cloud.metaapi.sdk.copy_factory;

import cloud.metaapi.sdk.clients.HttpClient;
import cloud.metaapi.sdk.clients.RetryOptions;
import cloud.metaapi.sdk.clients.copy_factory.ConfigurationClient;
import cloud.metaapi.sdk.clients.copy_factory.HistoryClient;
import cloud.metaapi.sdk.clients.copy_factory.TradingClient;

/**
 * MetaApi CopyFactory copy trading API SDK
 */
public class CopyFactory {
    
    private ConfigurationClient configurationClient;
    private HistoryClient historyClient;
    private TradingClient tradingClient;
    
    /**
     * CopyFactory options
     */
    public static class Options {
        /**
         * Domain to connect to, or {@code null}
         */
        public String domain;
        /**
         * Timeout for http requests in seconds, or {@code null}
         */
        public Integer requestTimeout;
        /**
         * Timeout for connecting to server in seconds, or {@code null}
         */
        public Integer connectTimeout;
        /**
         * Retry options
         */
        public RetryOptions retryOpts = new RetryOptions();
    }
    
    /**
     * Constructs CopyFactory class instance with default options
     * @param token authorization token
     */
    public CopyFactory(String token) {
        this(token, null);
    }
    
    /**
     * Constructs CopyFactory class instance
     * @param token authorization token
     * @param opts connection options, or {@code null}
     */
    public CopyFactory(String token, Options opts) {
        String domain = opts != null && opts.domain != null ? opts.domain : "agiliumtrade.agiliumtrade.ai";
        int requestTimeout = opts != null && opts.requestTimeout != null ? opts.requestTimeout : 60;
        int connectTimeout = opts != null && opts.connectTimeout != null ? opts.connectTimeout : 60;
        HttpClient httpClient = new HttpClient(requestTimeout * 1000, connectTimeout * 1000, opts.retryOpts);
        configurationClient = new ConfigurationClient(httpClient, token, domain);
        historyClient = new HistoryClient(httpClient, token, domain);
        tradingClient = new TradingClient(httpClient, token, domain);
    }
    
    /**
     * Returns CopyFactory configuration API
     * @return configuration API
     */
    public ConfigurationClient getConfigurationApi() {
        return configurationClient;
    }
    
    /**
     * Returns CopyFactory history API
     * @return history API
     */
    public HistoryClient getHistoryApi() {
        return historyClient;
    }
    
    /**
     * Returns CopyFactory trading API
     * @return trading API
     */
    public TradingClient getTradingApi() {
        return tradingClient;
    }
}