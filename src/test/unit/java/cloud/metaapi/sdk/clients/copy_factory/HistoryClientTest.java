package cloud.metaapi.sdk.clients.copy_factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cloud.metaapi.sdk.clients.HttpRequestOptions;
import cloud.metaapi.sdk.clients.HttpRequestOptions.Method;
import cloud.metaapi.sdk.clients.copy_factory.models.*;
import cloud.metaapi.sdk.clients.mocks.HttpClientMock;
import cloud.metaapi.sdk.clients.models.IsoTime;
import cloud.metaapi.sdk.util.JsonMapper;

/**
 * Tests {@link HistoryClient}
 */
class HistoryClientTest {

    private final static String copyFactoryApiUrl = "https://trading-api-v1.agiliumtrade.agiliumtrade.ai";
    private static ObjectMapper jsonMapper = JsonMapper.getInstance();
    private HistoryClient copyFactoryClient;
    private HttpClientMock httpClient = new HttpClientMock((opts) -> CompletableFuture.completedFuture("empty"));
    
    @BeforeEach
    void setUp() throws Exception {
        copyFactoryClient = new HistoryClient(httpClient, "header.payload.sign");
    }
    
    /**
     * Tests {@link HistoryClient#getProviders()}
     */
    @Test
    void testRetrieveProvidersFromApi() throws Exception {
        List<CopyFactorySubscriberOrProvider> expectedProviders = Lists.list(new CopyFactorySubscriberOrProvider() {{
            id = "577f095ab64b4d1710de34f6a28ab3bd";
            name = "First Last";
            strategies = Lists.list(new CopyFactoryStrategyIdAndName() {{
                id = "ABCD";
                name = "Test strategy";
            }});
        }});
        httpClient.setRequestMock((actualOptions) -> {
            try {
                HttpRequestOptions expectedOptions = new HttpRequestOptions(
                    copyFactoryApiUrl + "/users/current/providers", Method.GET);
                expectedOptions.getHeaders().put("auth-token", "header.payload.sign");
                assertThat(actualOptions).usingRecursiveComparison().isEqualTo(expectedOptions);
                return CompletableFuture.completedFuture(jsonMapper.writeValueAsString(expectedProviders));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        });
        List<CopyFactorySubscriberOrProvider> actualProviders = copyFactoryClient.getProviders().get();
        assertThat(actualProviders).usingRecursiveComparison().isEqualTo(expectedProviders);
    }
    
    /**
     * Tests {@link HistoryClient#getProviders()}
     */
    @Test
    void testDoesNotRetrieveProvidersFromApiWithAccountToken() throws Exception {
        copyFactoryClient = new HistoryClient(httpClient, "token");
        try {
            copyFactoryClient.getProviders().get();
        } catch (ExecutionException e) {
            assertEquals(
                "You can not invoke getProviders method, because you have connected with account access token. "
                + "Please use API access token from https://app.metaapi.cloud/token page to invoke this method.",
                e.getCause().getMessage()
            );
        };
    }
    
    /**
     * Tests {@link HistoryClient#getSubscribers()}
     */
    @Test
    void testRetrieveSubscribersFromApi() throws Exception {
        List<CopyFactorySubscriberOrProvider> expectedSubscribers = Lists.list(new CopyFactorySubscriberOrProvider() {{
            id = "577f095ab64b4d1710de34f6a28ab3bd";
            name = "First Last";
            strategies = Lists.list(new CopyFactoryStrategyIdAndName() {{
                id = "ABCD";
                name = "Test strategy";
            }});
        }});
        httpClient.setRequestMock((actualOptions) -> {
            try {
                HttpRequestOptions expectedOptions = new HttpRequestOptions(
                    copyFactoryApiUrl + "/users/current/subscribers", Method.GET);
                expectedOptions.getHeaders().put("auth-token", "header.payload.sign");
                assertThat(actualOptions).usingRecursiveComparison().isEqualTo(expectedOptions);
                return CompletableFuture.completedFuture(jsonMapper.writeValueAsString(expectedSubscribers));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        });
        List<CopyFactorySubscriberOrProvider> actualSubscribers = copyFactoryClient.getSubscribers().get();
        assertThat(actualSubscribers).usingRecursiveComparison().isEqualTo(expectedSubscribers);
    }
    
    /**
     * Tests {@link HistoryClient#getSubscribers()}
     */
    @Test
    void testDoesNotRetrieveSubscribersFromApiWithAccountToken() throws Exception {
        copyFactoryClient = new HistoryClient(httpClient, "token");
        try {
            copyFactoryClient.getSubscribers().get();
        } catch (ExecutionException e) {
            assertEquals(
                "You can not invoke getSubscribers method, because you have connected with account access token. "
                + "Please use API access token from https://app.metaapi.cloud/token page to invoke this method.",
                e.getCause().getMessage()
            );
        };
    }
    
    /**
     * Tests {@link HistoryClient#getStrategiesSubscribed()}
     */
    @Test
    void testRetrieveStrategiesSubscribedToFromApi() throws Exception {
        List<CopyFactoryStrategyIdAndName> expectedStrategies = Lists.list(new CopyFactoryStrategyIdAndName() {{
            id = "ABCD";
            name = "Test strategy";
        }});
        httpClient.setRequestMock((actualOptions) -> {
            try {
                HttpRequestOptions expectedOptions = new HttpRequestOptions(
                    copyFactoryApiUrl + "/users/current/strategies-subscribed", Method.GET);
                expectedOptions.getHeaders().put("auth-token", "header.payload.sign");
                assertThat(actualOptions).usingRecursiveComparison().isEqualTo(expectedOptions);
                return CompletableFuture.completedFuture(jsonMapper.writeValueAsString(expectedStrategies));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        });
        List<CopyFactoryStrategyIdAndName> actualStrategies = copyFactoryClient.getStrategiesSubscribed().get();
        assertThat(actualStrategies).usingRecursiveComparison().isEqualTo(expectedStrategies);
    }
    
    /**
     * Tests {@link HistoryClient#getStrategiesSubscribed()}
     */
    @Test
    void testDoesNotRetrieveStrategiesSubscribedToFromApiWithAccountToken() throws Exception {
        copyFactoryClient = new HistoryClient(httpClient, "token");
        try {
            copyFactoryClient.getStrategiesSubscribed().get();
        } catch (ExecutionException e) {
            assertEquals(
                "You can not invoke getStrategiesSubscribed method, because you have connected with account access token. "
                + "Please use API access token from https://app.metaapi.cloud/token page to invoke this method.",
                e.getCause().getMessage()
            );
        };
    }
    
    /**
     * Tests {@link HistoryClient#getProvidedStrategies()}
     */
    @Test
    void testRetrieveProvidedStrategiesFromApi() throws Exception {
        List<CopyFactoryStrategyIdAndName> expectedStrategies = Lists.list(new CopyFactoryStrategyIdAndName() {{
            id = "ABCD";
            name = "Test strategy";
        }});
        httpClient.setRequestMock((actualOptions) -> {
            try {
                HttpRequestOptions expectedOptions = new HttpRequestOptions(
                    copyFactoryApiUrl + "/users/current/provided-strategies", Method.GET);
                expectedOptions.getHeaders().put("auth-token", "header.payload.sign");
                assertThat(actualOptions).usingRecursiveComparison().isEqualTo(expectedOptions);
                return CompletableFuture.completedFuture(jsonMapper.writeValueAsString(expectedStrategies));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        });
        List<CopyFactoryStrategyIdAndName> actualStrategies = copyFactoryClient.getProvidedStrategies().get();
        assertThat(actualStrategies).usingRecursiveComparison().isEqualTo(expectedStrategies);
    }
    
    /**
     * Tests {@link HistoryClient#getProvidedStrategies()}
     */
    @Test
    void testDoesNotRetrieveProvidedStrategiesFromApiWithAccountToken() throws Exception {
        copyFactoryClient = new HistoryClient(httpClient, "token");
        try {
            copyFactoryClient.getProvidedStrategies().get();
        } catch (ExecutionException e) {
            assertEquals(
                "You can not invoke getProvidedStrategies method, because you have connected with account access token. "
                + "Please use API access token from https://app.metaapi.cloud/token page to invoke this method.",
                e.getCause().getMessage()
            );
        };
    }
    
    /**
     * Tests {@link HistoryClient#getProvidedStrategiesTransactions(IsoTime, IsoTime, List, List, Integer, Integer)}
     */
    @Test
    void testRetrieveTransactionsPerformedOnProvidedStrategiesFromApi() throws Exception {
        List<CopyFactoryTransaction> expectedTransactions = Lists.list(new CopyFactoryTransaction() {{
            id = "64664661:close";
            type = DealType.DEAL_TYPE_SELL;
            time = new IsoTime("2020-08-02T21:01:01.830Z");
            accountId = "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef";
            symbol = "EURJPY";
            subscriber = new CopyFactorySubscriberOrProvider() {{
                id = "subscriberId";
                name = "Subscriber";
            }};
            demo = false;
            provider = new CopyFactorySubscriberOrProvider() {{
                id = "providerId";
                name = "Provider";
            }};
            strategy = new CopyFactoryStrategyIdAndName() {{
                id = "ABCD";
            }};
            improvement = 0;
            providerCommission = 0;
            platformCommission = 0;
            quantity = -0.04;
            lotPrice = 117566.08744776;
            tickPrice = 124.526;
            amount = -4702.643497910401;
            commission = -0.14;
            swap = -0.14;
            profit = 0.49;
        }});
        IsoTime from = new IsoTime(Date.from(Instant.now()));
        IsoTime till = new IsoTime(Date.from(Instant.now()));
        httpClient.setRequestMock((actualOptions) -> {
            try {
                HttpRequestOptions expectedOptions = new HttpRequestOptions(
                    copyFactoryApiUrl + "/users/current/provided-strategies/transactions", Method.GET);
                expectedOptions.getHeaders().put("auth-token", "header.payload.sign");
                expectedOptions.getQueryParameters().put("from", from);
                expectedOptions.getQueryParameters().put("till", till);
                expectedOptions.getQueryParameters().put("strategyId", Lists.list("ABCD"));
                expectedOptions.getQueryParameters().put("subscriberId", Lists.list("subscriberId"));
                expectedOptions.getQueryParameters().put("offset", 100);
                expectedOptions.getQueryParameters().put("limit", 200);
                assertThat(actualOptions).usingRecursiveComparison().isEqualTo(expectedOptions);
                return CompletableFuture.completedFuture(jsonMapper.writeValueAsString(expectedTransactions));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        });
        List<CopyFactoryTransaction> actualStrategies = copyFactoryClient
            .getProvidedStrategiesTransactions(from, till, Lists.list("ABCD"), Lists.list("subscriberId"), 100, 200).get();
        assertThat(actualStrategies).usingRecursiveComparison().isEqualTo(expectedTransactions);
    }
    
    /**
     * Tests {@link HistoryClient#getProvidedStrategiesTransactions(IsoTime, IsoTime)}
     */
    @Test
    void testDoesNotRetrieveTransactionsOnProvidedStrategiesFromApiWithAccountToken() throws Exception {
        copyFactoryClient = new HistoryClient(httpClient, "token");
        try {
            IsoTime from = new IsoTime(Date.from(Instant.now()));
            IsoTime till = new IsoTime(Date.from(Instant.now()));
            copyFactoryClient.getProvidedStrategiesTransactions(from, till).get();
        } catch (ExecutionException e) {
            assertEquals(
                "You can not invoke getProvidedStrategiesTransactions method, because you have connected with account access token. "
                + "Please use API access token from https://app.metaapi.cloud/token page to invoke this method.",
                e.getCause().getMessage()
            );
        };
    }
    
    /**
     * Tests {@link HistoryClient#getStrategiesSubscribedTransactions(IsoTime, IsoTime, List, List, Integer, Integer)}
     */
    @Test
    void testRetrieveTransactionsPerformedOnStrategiesCurrentUserIsSubscribedToFromApi() throws Exception {
        List<CopyFactoryTransaction> expectedTransactions = Lists.list(new CopyFactoryTransaction() {{
            id = "64664661:close";
            type = DealType.DEAL_TYPE_SELL;
            time = new IsoTime("2020-08-02T21:01:01.830Z");
            accountId = "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef";
            symbol = "EURJPY";
            subscriber = new CopyFactorySubscriberOrProvider() {{
                id = "subscriberId";
                name = "Subscriber";
            }};
            demo = false;
            provider = new CopyFactorySubscriberOrProvider() {{
                id = "providerId";
                name = "Provider";
            }};
            strategy = new CopyFactoryStrategyIdAndName() {{
                id = "ABCD";
            }};
            improvement = 0;
            providerCommission = 0;
            platformCommission = 0;
            quantity = -0.04;
            lotPrice = 117566.08744776;
            tickPrice = 124.526;
            amount = -4702.643497910401;
            commission = -0.14;
            swap = -0.14;
            profit = 0.49;
        }});
        IsoTime from = new IsoTime(Date.from(Instant.now()));
        IsoTime till = new IsoTime(Date.from(Instant.now()));
        httpClient.setRequestMock((actualOptions) -> {
            try {
                HttpRequestOptions expectedOptions = new HttpRequestOptions(
                    copyFactoryApiUrl + "/users/current/strategies-subscribed/transactions", Method.GET);
                expectedOptions.getHeaders().put("auth-token", "header.payload.sign");
                expectedOptions.getQueryParameters().put("from", from);
                expectedOptions.getQueryParameters().put("till", till);
                expectedOptions.getQueryParameters().put("strategyId", Lists.list("ABCD"));
                expectedOptions.getQueryParameters().put("providerId", Lists.list("providerId"));
                expectedOptions.getQueryParameters().put("offset", 100);
                expectedOptions.getQueryParameters().put("limit", 200);
                assertThat(actualOptions).usingRecursiveComparison().isEqualTo(expectedOptions);
                return CompletableFuture.completedFuture(jsonMapper.writeValueAsString(expectedTransactions));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        });
        List<CopyFactoryTransaction> actualStrategies = copyFactoryClient
            .getStrategiesSubscribedTransactions(from, till, Lists.list("ABCD"), Lists.list("providerId"), 100, 200).get();
        assertThat(actualStrategies).usingRecursiveComparison().isEqualTo(expectedTransactions);
    }
    
    /**
     * Tests {@link HistoryClient#getStrategiesSubscribedTransactions(IsoTime, IsoTime)}
     */
    @Test
    void testDoesNotRetrieveTransactionsOnStrategiesSubscribedToFromApiWithAccountToken() throws Exception {
        copyFactoryClient = new HistoryClient(httpClient, "token");
        try {
            IsoTime from = new IsoTime(Date.from(Instant.now()));
            IsoTime till = new IsoTime(Date.from(Instant.now()));
            copyFactoryClient.getStrategiesSubscribedTransactions(from, till).get();
        } catch (ExecutionException e) {
            assertEquals(
                "You can not invoke getStrategiesSubscribedTransactions method, because you have connected with account access token. "
                + "Please use API access token from https://app.metaapi.cloud/token page to invoke this method.",
                e.getCause().getMessage()
            );
        };
    }
}