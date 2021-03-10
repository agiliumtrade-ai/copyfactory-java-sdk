package cloud.metaapi.sdk.clients.copy_factory;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.apache.commons.lang3.RandomStringUtils;

import cloud.metaapi.sdk.clients.HttpClient;
import cloud.metaapi.sdk.clients.HttpRequestOptions;
import cloud.metaapi.sdk.clients.HttpRequestOptions.Method;
import cloud.metaapi.sdk.clients.copy_factory.models.*;
import cloud.metaapi.sdk.clients.MetaApiClient;
import cloud.metaapi.sdk.clients.TimeoutException;

/**
 * metaapi.cloud CopyFactory configuration API (trade copying configuration API) client (see
 * https://trading-api-v1.project-stock.agiliumlabs.cloud/swagger/#/)
 */
public class ConfigurationClient extends MetaApiClient {

    /**
     * Constructs CopyFactory configuration API client instance. Domain is set to {@code agiliumtrade.agiliumtrade.ai}
     * @param httpClient HTTP client
     * @param token authorization token
     */
    public ConfigurationClient(HttpClient httpClient, String token) {
        this(httpClient, token, "agiliumtrade.agiliumtrade.ai");
    }

    /**
     * Constructs CopyFactory configuration API client instance
     * @param httpClient HTTP client
     * @param token authorization token
     * @param domain domain to connect to
     */
    public ConfigurationClient(HttpClient httpClient, String token, String domain) {
        super(httpClient, token, domain);
        this.host = "https://trading-api-v1." + domain;
    }
    
    /**
     * Retrieves new unused strategy id. Method is accessible only with API access token. See
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/get_users_current_configuration_unused_strategy_id
     * @return completable future resolving with strategy id generated
     */
    public CompletableFuture<StrategyId> generateStrategyId() {
        if (isNotJwtToken()) return handleNoAccessError("generateStrategyId");
        HttpRequestOptions opts = new HttpRequestOptions(
            host + "/users/current/configuration/unused-strategy-id", Method.GET);
        opts.getHeaders().put("auth-token", token);
        return httpClient.requestJson(opts, StrategyId.class);
    }
    
    /**
     * Generates random account id
     * @return account id
     */
    public String generateAccountId() {
        return RandomStringUtils.randomAlphanumeric(64);
    }
    
    /**
     * Retrieves CopyFactory copy trading accounts. See
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/get_users_current_configuration_accounts
     * @return completable future resolving with CopyFactory accounts found
     */
    public CompletableFuture<List<CopyFactoryAccount>> getAccounts() {
        if (isNotJwtToken()) return handleNoAccessError("getAccounts");
        HttpRequestOptions opts = new HttpRequestOptions(
            host + "/users/current/configuration/accounts", Method.GET);
        opts.getHeaders().put("auth-token", token);
        return httpClient.requestJson(opts, CopyFactoryAccount[].class).thenApply((array) -> Arrays.asList(array));
    }
    
    /**
     * Updates a CopyFactory trade copying account. See
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/put_users_current_configuration_accounts_accountId
     * @param id copy trading account id
     * @param account trading account update
     * @return completable future resolving when account is updated
     */
    public CompletableFuture<Void> updateAccount(String id, CopyFactoryAccountUpdate account) {
        if (isNotJwtToken()) return handleNoAccessError("updateAccount");
        HttpRequestOptions opts = new HttpRequestOptions(
            host + "/users/current/configuration/accounts/" + id, Method.PUT);
        opts.getHeaders().put("auth-token", token);
        opts.setBody(account);
        return httpClient.request(opts).thenApply((response) -> null);
    }
    
    /**
     * Deletes a CopyFactory trade copying account. See
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/delete_users_current_configuration_accounts_accountId
     * @param id copy trading account id
     * @return completable future resolving when account is removed
     */
    public CompletableFuture<Void> removeAccount(String id) {
        if (isNotJwtToken()) return handleNoAccessError("removeAccount");
        HttpRequestOptions opts = new HttpRequestOptions(
            host + "/users/current/configuration/accounts/" + id, Method.DELETE);
        opts.getHeaders().put("auth-token", token);
        return httpClient.request(opts).thenApply((response) -> null);
    }
    
    /**
     * Retrieves CopyFactory copy trading strategies. See
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/get_users_current_configuration_strategies
     * @return completable future resolving with CopyFactory strategies found
     */
    public CompletableFuture<List<CopyFactoryStrategy>> getStrategies() {
        if (isNotJwtToken()) return handleNoAccessError("getStrategies");
        HttpRequestOptions opts = new HttpRequestOptions(
            host + "/users/current/configuration/strategies", Method.GET);
        opts.getHeaders().put("auth-token", token);
        return httpClient.requestJson(opts, CopyFactoryStrategy[].class).thenApply(array -> Arrays.asList(array));
    }
    
    /**
     * Updates a CopyFactory strategy. See
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/put_users_current_configuration_strategies_strategyId
     * @param id copy trading strategy id
     * @param strategy trading strategy update
     * @return completable future resolving when strategy is updated
     */
    public CompletableFuture<Void> updateStrategy(String id, CopyFactoryStrategyUpdate strategy) {
        if (isNotJwtToken()) return handleNoAccessError("updateStrategy");
        HttpRequestOptions opts = new HttpRequestOptions(
            host + "/users/current/configuration/strategies/" + id, Method.PUT);
        opts.getHeaders().put("auth-token", token);
        opts.setBody(strategy);
        return httpClient.request(opts).thenApply((response) -> null);
    }
    
    /**
     * Deletes a CopyFactory strategy. See
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/delete_users_current_configuration_strategies_strategyId
     * @param id strategy id
     * @return completable future resolving when strategy is removed
     */
    public CompletableFuture<Void> removeStrategy(String id) {
        if (isNotJwtToken()) return handleNoAccessError("removeStrategy");
        HttpRequestOptions opts = new HttpRequestOptions(
            host + "/users/current/configuration/strategies/" + id, Method.DELETE);
        opts.getHeaders().put("auth-token", token);
        return httpClient.request(opts).thenApply((response) -> null);
    }
    
    /**
     * Retrieves CopyFactory copy portfolio strategies. See
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/get_users_current_configuration_portfolio_strategies
     * @return completable future resolving with CopyFactory portfolio strategies found
     */
    public CompletableFuture<List<CopyFactoryPortfolioStrategy>> getPortfolioStrategies() {
        if (isNotJwtToken()) return handleNoAccessError("getPortfolioStrategies");
        HttpRequestOptions opts = new HttpRequestOptions(
            host + "/users/current/configuration/portfolio-strategies", Method.GET);
        opts.getHeaders().put("auth-token", token);
        return httpClient.requestJson(opts, CopyFactoryPortfolioStrategy[].class)
            .thenApply(array -> Arrays.asList(array));
    }
    
    /**
     * Updates a CopyFactory portfolio strategy. See
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/put_users_current_configuration_portfolio_strategies_portfolioId
     * @param id copy trading portfolio strategy id
     * @param strategy portfolio strategy update
     * @return completable future resolving when portfolio strategy is updated
     */
    public CompletableFuture<Void> updatePortfolioStrategy(String id, CopyFactoryPortfolioStrategyUpdate strategy) {
        if (isNotJwtToken()) return handleNoAccessError("updatePortfolioStrategy");
        HttpRequestOptions opts = new HttpRequestOptions(
            host + "/users/current/configuration/portfolio-strategies/" + id, Method.PUT);
        opts.getHeaders().put("auth-token", token);
        opts.setBody(strategy);
        return httpClient.request(opts).thenApply((response) -> null);
    }
    
    /**
     * Deletes a CopyFactory portfolio strategy. See
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/
     * #!/default/delete_users_current_configuration_portfolio_strategies_portfolioId
     * @param id portfolio strategy id
     * @return completable future resolving when portfolio strategy is removed
     */
    public CompletableFuture<Void> removePortfolioStrategy(String id) {
        if (isNotJwtToken()) return handleNoAccessError("removePortfolioStrategy");
        HttpRequestOptions opts = new HttpRequestOptions(
            host + "/users/current/configuration/portfolio-strategies/" + id, Method.DELETE);
        opts.getHeaders().put("auth-token", token);
        return httpClient.request(opts).thenApply((response) -> null);
    }
    
    /**
     * Returns list of active resynchronization tasks for a specified connection. See
     * https://trading-api-v1.agiliumtrade.agiliumtrade.ai/swagger/#!/default/get_users_current_configuration_connections_connectionId_active_resynchronization_tasks
     * @param connectionId MetaApi account id to return tasks for
     * @return completable future resolving with list of active resynchronization tasks
     */
    public CompletableFuture<List<ResynchronizationTask>> getActiveResynchronizationTasks(String connectionId) {
        if (isNotJwtToken()) return handleNoAccessError("getActiveResynchronizationTasks");
        HttpRequestOptions opts = new HttpRequestOptions(host + "/users/current/configuration/connections/" 
            + connectionId + "/active-resynchronization-tasks", Method.GET);
        opts.getHeaders().put("auth-token", token);
        return httpClient.requestJson(opts, ResynchronizationTask[].class).thenApply(array -> Arrays.asList(array));
    }
    
    /**
     * Waits until active resynchronization tasks are completed with default timeout and reloading interval.
     * Completes exceptionally with {@link TimeoutException} if tasks have not completed  to the broker withing
     * timeout allowed.
     * @param connectionId MetaApi account id to wait tasks completed for
     * @return completable future which resolves when tasks are completed
     */
    public CompletableFuture<Void> waitResynchronizationTasksCompleted(String connectionId) {
        return waitResynchronizationTasksCompleted(connectionId, null, null);
    }
    
    /**
     * Waits until active resynchronization tasks are completed. Completes exceptionally with
     * {@link TimeoutException} if tasks have not completed  to the broker withing timeout allowed.
     * @param connectionId MetaApi account id to wait tasks completed for
     * @param timeoutInSeconds wait timeout in seconds, default is 5m
     * @param intervalInMilliseconds interval between tasks reload while waiting for a change, default is 1s
     * @return completable future which resolves when tasks are completed
     */
    public CompletableFuture<Void> waitResynchronizationTasksCompleted(
        String connectionId, Integer timeoutInSeconds, Integer intervalInMilliseconds) {
        return CompletableFuture.runAsync(() -> {
            try {
                long startTime = Instant.now().getEpochSecond();
                long timeoutTime = startTime + (timeoutInSeconds != null ? timeoutInSeconds : 300);
                List<ResynchronizationTask> tasks = getActiveResynchronizationTasks(connectionId).get();
                while (!tasks.isEmpty() && timeoutTime > Instant.now().getEpochSecond()) {
                    Thread.sleep(intervalInMilliseconds != null ? intervalInMilliseconds : 1000);
                    tasks = getActiveResynchronizationTasks(connectionId).get();
                };
                if (!tasks.isEmpty()) {
                    throw new TimeoutException("Timed out waiting for resynchronization tasks for account "
                        + connectionId + " to be completed");
                }
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }
}