package cloud.metaapi.sdk.copy_factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cloud.metaapi.sdk.clients.HttpClient;
import cloud.metaapi.sdk.clients.RetryOptions;
import cloud.metaapi.sdk.clients.copy_factory.ConfigurationClient;
import cloud.metaapi.sdk.clients.copy_factory.HistoryClient;
import cloud.metaapi.sdk.clients.copy_factory.TradingClient;
import cloud.metaapi.sdk.clients.error_handler.ValidationException;

/**
 * MetaApi CopyFactory copy trading API SDK
 */
public class CopyFactory {
  
  private static Logger logger = LogManager.getLogger(CopyFactory.class);
  private ConfigurationClient configurationClient;
  private HistoryClient historyClient;
  private TradingClient tradingClient;
  
  /**
   * CopyFactory options
   */
  public static class Options {
    /**
     * Domain to connect to
     */
    public String domain = "agiliumtrade.agiliumtrade.ai";
    /**
     * Timeout for http requests in seconds
     */
    public int requestTimeout = 60;
    /**
     * Timeout for connecting to server in seconds
     */
    public int connectTimeout = 60;
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
    try {
      initialize(token, null);
    } catch (ValidationException e) {
      logger.error("Specified options are invalid", e);
    }
  }
  
  /**
   * Constructs CopyFactory class instance
   * @param token authorization token
   * @param opts connection options
   * @throws ValidationException if specified options are invalid
   */
  public CopyFactory(String token, Options opts) throws ValidationException {
    initialize(token, opts);
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
  
  private void initialize(String token, Options opts) throws ValidationException {
    HttpClient httpClient = new HttpClient(opts.requestTimeout * 1000, opts.connectTimeout * 1000, opts.retryOpts);
    configurationClient = new ConfigurationClient(httpClient, token, opts.domain);
    historyClient = new HistoryClient(httpClient, token, opts.domain);
    tradingClient = new TradingClient(httpClient, token, opts.domain);
  }
}