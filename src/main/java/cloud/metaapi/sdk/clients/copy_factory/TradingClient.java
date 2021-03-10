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
 * metaapi.cloud CopyFactory trading API (trade copying trading API) client (see
 * https://trading-api-v1.project-stock.agiliumlabs.cloud/swagger/#/)
 */
public class TradingClient extends MetaApiClient {

    /**
     * Constructs CopyFactory trading API client instance. Domain is set to {@code agiliumtrade.agiliumtrade.ai}
     * @param httpClient HTTP client
     * @param token authorization token
     */
    public TradingClient(HttpClient httpClient, String token) {
        this(httpClient, token, "agiliumtrade.agiliumtrade.ai");
    }

    /**
     * Constructs CopyFactory trading API client instance
     * @param httpClient HTTP client
     * @param token authorization token
     * @param domain domain to connect to
     */
    public TradingClient(HttpClient httpClient, String token, String domain) {
        super(httpClient, token, domain);
        this.host = "https://trading-api-v1." + domain;
    }
    
    /**
     * Resynchronizes the account. See
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/post_users_current_accounts_accountId_resynchronize
     * @param accountId account id
     * @param strategyIds optional array of strategy ids to recynchronize, or {@code null}.
     * Default is to synchronize all strategies
     * @return completable future which resolves when resynchronization is scheduled
     */
    public CompletableFuture<Void> resynchronize(String accountId, List<String> strategyIds) {
        if (isNotJwtToken()) return handleNoAccessError("resynchronize");
        HttpRequestOptions opts = new HttpRequestOptions(
            host + "/users/current/accounts/" + accountId + "/resynchronize", Method.POST);
        opts.getHeaders().put("auth-token", token);
        if (strategyIds != null && !strategyIds.isEmpty()) opts.getQueryParameters().put("strategyId", strategyIds);
        return httpClient.request(opts).thenApply(response -> null);
    }
    
    /**
     * Returns subscriber account stopouts. See
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/get_users_current_accounts_accountId_stopouts
     * @param accountId account id
     * @return completable future which resolves with stopouts found
     */
    public CompletableFuture<List<CopyFactoryStrategyStopout>> getStopouts(String accountId) {
        if (isNotJwtToken()) return handleNoAccessError("getStopouts");
        HttpRequestOptions opts = new HttpRequestOptions(
            host + "/users/current/accounts/" + accountId + "/stopouts", Method.GET);
        opts.getHeaders().put("auth-token", token);
        return httpClient.requestJson(opts, CopyFactoryStrategyStopout[].class)
            .thenApply((array) -> Arrays.asList(array));
    }
    
    /**
     * Resets strategy stopouts. See
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/post_users_current_accounts_accountId_
     * strategies_subscribed_strategyId_stopouts_reason_reset
     * @param accountId account id
     * @param strategyId strategy id
     * @param reason stopout reason to reset. One of yearly-balance, monthly-balance, daily-balance,
     * yearly-equity, monthly-equity, daily-equity, max-drawdown
     * @return completable future which resolves when the stopouts are reset
     */
    public CompletableFuture<Void> resetStopouts(String accountId, String strategyId, String reason) {
        if (isNotJwtToken()) return handleNoAccessError("resetStopouts");
        HttpRequestOptions opts = new HttpRequestOptions(host + "/users/current/accounts/" + accountId
            + "/strategies-subscribed/" + strategyId + "/stopouts/" + reason + "/reset", Method.POST);
        opts.getHeaders().put("auth-token", token);
        return httpClient.request(opts).thenApply(response -> null);
    }
    
    /**
     * Returns copy trading user log for an account and time range. See
     * https://trading-api-v1.project-stock.v2.agiliumlabs.cloud/swagger/#!/default/get_users_current_accounts_accountId_user_log
     * @param accountId account id
     * @return completable future which resolves with log records found
     */
    public CompletableFuture<List<CopyFactoryUserLogRecord>> getUserLog(String accountId) {
        return getUserLog(accountId, null, null, null, null);
    }
    
    /**
     * Returns copy trading user log for an account and time range. See
     * https://trading-api-v1.project-stock.v2.agiliumlabs.cloud/swagger/#!/default/get_users_current_accounts_accountId_user_log
     * @param accountId account id
     * @param startTime time to start loading data from, or {@code null}
     * @param endTime time to stop loading data at, or {@code null}
     * @return completable future which resolves with log records found
     */
    public CompletableFuture<List<CopyFactoryUserLogRecord>> getUserLog(String accountId,
        IsoTime startTime, IsoTime endTime) {
        return getUserLog(accountId, startTime, endTime, null, null);
    }
    
    /**
     * Returns copy trading user log for an account and time range. See
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/get_users_current_accounts_accountId_user_log
     * @param accountId account id
     * @param startTime time to start loading data from, or {@code null}
     * @param endTime time to stop loading data at, or {@code null}
     * @param offset pagination offset, or {@code null}. Default is 0
     * @param limit pagination limit, or {@code null}. Default is 1000
     * @return completable future which resolves with log records found
     */
    public CompletableFuture<List<CopyFactoryUserLogRecord>> getUserLog(String accountId,
        IsoTime startTime, IsoTime endTime, Integer offset, Integer limit) {
        if (isNotJwtToken()) return handleNoAccessError("getUserLog");
        HttpRequestOptions opts = new HttpRequestOptions(
            host + "/users/current/accounts/" + accountId + "/user-log", Method.GET);
        opts.getHeaders().put("auth-token", token);
        if (startTime != null) opts.getQueryParameters().put("startTime", startTime);
        if (endTime != null) opts.getQueryParameters().put("endTime", endTime);
        if (offset != null) opts.getQueryParameters().put("offset", offset);
        if (limit != null) opts.getQueryParameters().put("limit", limit);
        return httpClient.requestJson(opts, CopyFactoryUserLogRecord[].class)
            .thenApply((array) -> Arrays.asList(array));
    }
}