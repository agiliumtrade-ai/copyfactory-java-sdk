package cloud.metaapi.sdk.clients.copy_factory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import cloud.metaapi.sdk.clients.HttpClient;
import cloud.metaapi.sdk.clients.HttpRequestOptions;
import cloud.metaapi.sdk.clients.MetaApiClient;
import cloud.metaapi.sdk.clients.HttpRequestOptions.Method;
import cloud.metaapi.sdk.clients.copy_factory.models.*;
import cloud.metaapi.sdk.clients.models.IsoTime;

/**
 * metaapi.cloud CopyFactory history API (trade copying history API) client (see
 * https://trading-api-v1.project-stock.agiliumlabs.cloud/swagger/#/)
 */
public class HistoryClient extends MetaApiClient {

    /**
     * Constructs CopyFactory history API client instance. Domain is set to {@code agiliumtrade.agiliumtrade.ai}
     * @param httpClient HTTP client
     * @param token authorization token
     */
    public HistoryClient(HttpClient httpClient, String token) {
        this(httpClient, token, "agiliumtrade.agiliumtrade.ai");
    }

    /**
     * Constructs CopyFactory history API client instance
     * @param httpClient HTTP client
     * @param token authorization token
     * @param domain domain to connect to
     */
    public HistoryClient(HttpClient httpClient, String token, String domain) {
        super(httpClient, token, domain);
        this.host = "https://trading-api-v1." + domain;
    }
    
    /**
     * Returns list of providers providing strategies to the current user
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/get_users_current_providers
     * @return completable future resolving with providers found
     */
    public CompletableFuture<List<CopyFactorySubscriberOrProvider>> getProviders() {
        if (isNotJwtToken()) return handleNoAccessError("getProviders");
        HttpRequestOptions opts = new HttpRequestOptions(host + "/users/current/providers", Method.GET);
        opts.getHeaders().put("auth-token", token);
        return httpClient.requestJson(opts, CopyFactorySubscriberOrProvider[].class)
            .thenApply((array) -> Arrays.asList(array));
    }
    
    /**
     * Returns list of subscribers subscribed to the strategies of the current user
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/get_users_current_subscribers
     * @return completable future resolving with subscribers found
     */
    public CompletableFuture<List<CopyFactorySubscriberOrProvider>> getSubscribers() {
        if (isNotJwtToken()) return handleNoAccessError("getSubscribers");
        HttpRequestOptions opts = new HttpRequestOptions(host + "/users/current/subscribers", Method.GET);
        opts.getHeaders().put("auth-token", token);
        return httpClient.requestJson(opts, CopyFactorySubscriberOrProvider[].class)
            .thenApply((array) -> Arrays.asList(array));
    }
    
    /**
     * Returns list of strategies the current user is subscribed to
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/get_users_current_strategies_subscribed
     * @return completable future resolving with strategies found
     */
    public CompletableFuture<List<CopyFactoryStrategyIdAndName>> getStrategiesSubscribed() {
        if (isNotJwtToken()) return handleNoAccessError("getStrategiesSubscribed");
        HttpRequestOptions opts = new HttpRequestOptions(host + "/users/current/strategies-subscribed", Method.GET);
        opts.getHeaders().put("auth-token", token);
        return httpClient.requestJson(opts, CopyFactoryStrategyIdAndName[].class)
            .thenApply((array) -> Arrays.asList(array));
    }
    
    /**
     * Returns list of strategies the current user provides to other users
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/get_users_current_provided_strategies
     * @return completable future resolving with strategies found
     */
    public CompletableFuture<List<CopyFactoryStrategyIdAndName>> getProvidedStrategies() {
        if (isNotJwtToken()) return handleNoAccessError("getProvidedStrategies");
        HttpRequestOptions opts = new HttpRequestOptions(host + "/users/current/provided-strategies", Method.GET);
        opts.getHeaders().put("auth-token", token);
        return httpClient.requestJson(opts, CopyFactoryStrategyIdAndName[].class)
            .thenApply((array) -> Arrays.asList(array));
    }
    
    /**
     * Returns list of transactions on the strategies the current user provides to other users.
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/get_users_current_provided_strategies_transactions
     * Pagination offset is 0 and limit is default 1000. Does not filter by strategies or subscribers.
     * @param from time to load transactions from
     * @param till time to load transactions till
     * @return completable future resolving with transactions found
     */
    public CompletableFuture<List<CopyFactoryTransaction>> getProvidedStrategiesTransactions(
        IsoTime from, IsoTime till
    ) {
        return getProvidedStrategiesTransactions(from, till, null, null, null, null);
    }
    
    /**
     * Returns list of transactions on the strategies the current user provides to other users.
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/get_users_current_provided_strategies_transactions
     * Pagination offset is 0 and limit is default 1000.
     * @param from time to load transactions from
     * @param till time to load transactions till
     * @param strategyIds optional list of strategy ids to filter transactions by, or {@code null}
     * @param subscriberIds optional list of subscribers to filter transactions by, or {@code null}
     * @return completable future resolving with transactions found
     */
    public CompletableFuture<List<CopyFactoryTransaction>> getProvidedStrategiesTransactions(
        IsoTime from, IsoTime till, List<String> strategyIds, List<String> subscriberIds
    ) {
        return getProvidedStrategiesTransactions(from, till, strategyIds, subscriberIds, null, null);
    }
    
    /**
     * Returns list of transactions on the strategies the current user provides to other users
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/get_users_current_provided_strategies_transactions
     * @param from time to load transactions from
     * @param till time to load transactions till
     * @param strategyIds optional list of strategy ids to filter transactions by, or {@code null}
     * @param subscriberIds optional list of subscribers to filter transactions by, or {@code null}
     * @param offset pagination offset, or {@code null}. Default value is 0
     * @param limit pagination limit, or {@code null}. Default value is 10000
     * @return completable future resolving with transactions found
     */
    public CompletableFuture<List<CopyFactoryTransaction>> getProvidedStrategiesTransactions(
        IsoTime from, IsoTime till, List<String> strategyIds, List<String> subscriberIds, Integer offset, Integer limit
    ) {
        if (isNotJwtToken()) return handleNoAccessError("getProvidedStrategiesTransactions");
        HttpRequestOptions opts = new HttpRequestOptions(
            host + "/users/current/provided-strategies/transactions", Method.GET);
        opts.getHeaders().put("auth-token", token);
        opts.getQueryParameters().put("from", from);
        opts.getQueryParameters().put("till", till);
        if (strategyIds != null && !strategyIds.isEmpty())
            opts.getQueryParameters().put("strategyId", strategyIds);
        if (subscriberIds != null && !subscriberIds.isEmpty())
            opts.getQueryParameters().put("subscriberId", subscriberIds);
        if (offset != null) opts.getQueryParameters().put("offset", offset);
        if (limit != null) opts.getQueryParameters().put("limit", limit);
        return httpClient.requestJson(opts, CopyFactoryTransaction[].class)
            .thenApply((array) -> Arrays.asList(array));
    }
    
    /**
     * Returns list of trades on the strategies the current user subscribed to
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/get_users_current_strategies_subscribed_transactions
     * Pagination offset is 0 and limit is default 1000. Does not filter by strategies or providers.
     * @param from time to load transactions from
     * @param till time to load transactions till
     * @return completable future resolving with transactions found
     */
    public CompletableFuture<List<CopyFactoryTransaction>> getStrategiesSubscribedTransactions(
        IsoTime from, IsoTime till
    ) {
        return getStrategiesSubscribedTransactions(from, till, null, null, null, null);
    }
    
    /**
     * Returns list of trades on the strategies the current user subscribed to
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/get_users_current_strategies_subscribed_transactions
     * Pagination offset is 0 and limit is default 1000.
     * @param from time to load transactions from
     * @param till time to load transactions till
     * @param strategyIds optional list of strategy ids to filter transactions by, or {@code null}
     * @param providerIds optional list of providers to filter transactions by, or {@code null}
     * @return completable future resolving with transactions found
     */
    public CompletableFuture<List<CopyFactoryTransaction>> getStrategiesSubscribedTransactions(
        IsoTime from, IsoTime till, List<String> strategyIds, List<String> providerIds
    ) {
        return getStrategiesSubscribedTransactions(from, till, strategyIds, providerIds, null, null);
    }
    
    /**
     * Returns list of trades on the strategies the current user subscribed to
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/get_users_current_strategies_subscribed_transactions
     * @param from time to load transactions from
     * @param till time to load transactions till
     * @param strategyIds optional list of strategy ids to filter transactions by, or {@code null}
     * @param providerIds optional list of providers to filter transactions by, or {@code null}
     * @param offset pagination offset, or {@code null}. Default value is 0
     * @param limit pagination limit, or {@code null}. Default value is 10000
     * @return completable future resolving with transactions found
     */
    public CompletableFuture<List<CopyFactoryTransaction>> getStrategiesSubscribedTransactions(
        IsoTime from, IsoTime till, List<String> strategyIds, List<String> providerIds, Integer offset, Integer limit
    ) {
        if (isNotJwtToken()) return handleNoAccessError("getStrategiesSubscribedTransactions");
        HttpRequestOptions opts = new HttpRequestOptions(
            host + "/users/current/strategies-subscribed/transactions", Method.GET);
        opts.getHeaders().put("auth-token", token);
        opts.getQueryParameters().put("from", from);
        opts.getQueryParameters().put("till", till);
        if (strategyIds != null && !strategyIds.isEmpty())
            opts.getQueryParameters().put("strategyId", strategyIds);
        if (providerIds != null && !providerIds.isEmpty())
            opts.getQueryParameters().put("providerId", providerIds);
        if (offset != null) opts.getQueryParameters().put("offset", offset);
        if (limit != null) opts.getQueryParameters().put("limit", limit);
        return httpClient.requestJson(opts, CopyFactoryTransaction[].class)
            .thenApply((array) -> Arrays.asList(array));
    }
}