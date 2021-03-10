package cloud.metaapi.sdk.clients.copy_factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cloud.metaapi.sdk.clients.HttpRequestOptions;
import cloud.metaapi.sdk.clients.HttpRequestOptions.Method;
import cloud.metaapi.sdk.clients.TimeoutException;
import cloud.metaapi.sdk.clients.mocks.HttpClientMock;
import cloud.metaapi.sdk.clients.models.IsoTime;
import cloud.metaapi.sdk.util.JsonMapper;
import cloud.metaapi.sdk.clients.copy_factory.models.*;

/**
 * Tests {@link ConfigurationClient}
 */
class ConfigurationClientTest {

    private final static String copyFactoryApiUrl = "https://trading-api-v1.agiliumtrade.agiliumtrade.ai";
    private static ObjectMapper jsonMapper = JsonMapper.getInstance();
    private ConfigurationClient copyFactoryClient;
    private HttpClientMock httpClient;
    
    @BeforeEach
    void setUp() throws Exception {
        httpClient = new HttpClientMock((opts) -> CompletableFuture.completedFuture("empty"));
        copyFactoryClient = new ConfigurationClient(httpClient, "header.payload.sign");
    }

    /**
     * Tests {@link ConfigurationClient#generateAccountId()}
     */
    @Test
    void testGeneratesAccountId() {
        assertEquals(64, copyFactoryClient.generateAccountId().length());
    }
    
    /**
     * Tests {@link ConfigurationClient#updateAccount(String, CopyFactoryAccountUpdate)}
     */
    @Test
    void testUpdatesCopyFactoryAccountViaApi() throws Exception {
        httpClient.setRequestMock((actualOptions) -> {
            HttpRequestOptions expectedOptions = new HttpRequestOptions(
                copyFactoryApiUrl + "/users/current/configuration/accounts/" + 
                    "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef", Method.PUT);
            expectedOptions.getHeaders().put("auth-token", "header.payload.sign");
            expectedOptions.setBody(new CopyFactoryAccountUpdate() {{
                name = "Demo account";
                connectionId = "e8867baa-5ec2-45ae-9930-4d5cea18d0d6";
                reservedMarginFraction = 0.25;
                subscriptions = Lists.list(new CopyFactoryStrategySubscription() {{
                    strategyId = "ABCD";
                    multiplier = 1.0;
                }});
            }});
            assertThat(actualOptions).usingRecursiveComparison().isEqualTo(expectedOptions);
            return CompletableFuture.completedFuture(null);
        });
        copyFactoryClient.updateAccount("0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef", 
            new CopyFactoryAccountUpdate() {{
                name = "Demo account";
                connectionId = "e8867baa-5ec2-45ae-9930-4d5cea18d0d6";
                reservedMarginFraction = 0.25;
                subscriptions = Lists.list(new CopyFactoryStrategySubscription() {{
                    strategyId = "ABCD";
                    multiplier = 1.0;
                }});
            }}
        ).get();
    }
    
    /**
     * Tests {@link ConfigurationClient#updateAccount(String, CopyFactoryAccountUpdate)}
     */
    @Test
    void testDoesNotUpdatesCopyFactoryAccountViaApiWithAccountToken() throws Exception {
        copyFactoryClient = new ConfigurationClient(httpClient, "token");
        try {
            copyFactoryClient.updateAccount("id", new CopyFactoryAccountUpdate() {{}}).get();
        } catch (ExecutionException e) {
            assertEquals(
                "You can not invoke updateAccount method, because you have connected with account access token. "
                + "Please use API access token from https://app.metaapi.cloud/token page to invoke this method.",
                e.getCause().getMessage()
            );
        };
    }
    
    /**
     * Tests {@link ConfigurationClient#getAccounts()}
     */
    @Test
    void testRetrieveCopyFactoryAccountsFromApi() throws Exception {
        List<CopyFactoryAccount> expectedAccounts = Lists.list(new CopyFactoryAccount() {{
            _id = "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef";
            name = "Demo account";
            connectionId = "e8867baa-5ec2-45ae-9930-4d5cea18d0d6";
            reservedMarginFraction = 0.25;
            subscriptions = Lists.list(new CopyFactoryStrategySubscription() {{
                strategyId = "ABCD";
                multiplier = 1.0;
            }});
        }});
        httpClient.setRequestMock((actualOptions) -> {
            try {
                HttpRequestOptions expectedOptions = new HttpRequestOptions(
                    copyFactoryApiUrl + "/users/current/configuration/accounts", Method.GET);
                expectedOptions.getHeaders().put("auth-token", "header.payload.sign");
                assertThat(actualOptions).usingRecursiveComparison().isEqualTo(expectedOptions);
                return CompletableFuture.completedFuture(jsonMapper.writeValueAsString(expectedAccounts));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        });
        List<CopyFactoryAccount> actualAccounts = copyFactoryClient.getAccounts().get();
        assertThat(actualAccounts).usingRecursiveComparison().isEqualTo(expectedAccounts);
    }
    
    /**
     * Tests {@link ConfigurationClient#getAccounts()}
     */
    @Test
    void testDoesNotRetrieveCopyFactoryAccountsFromApiWithAccountToken() throws Exception {
        copyFactoryClient = new ConfigurationClient(httpClient, "token");
        try {
            copyFactoryClient.getAccounts().get();
        } catch (ExecutionException e) {
            assertEquals(
                "You can not invoke getAccounts method, because you have connected with account access token. "
                + "Please use API access token from https://app.metaapi.cloud/token page to invoke this method.",
                e.getCause().getMessage()
            );
        };
    }
    
    /**
     * Tests {@link ConfigurationClient#removeAccount(String)}
     */
    @Test
    void testRemovesCopyFactoryAccountViaApi() throws Exception {
        httpClient.setRequestMock((actualOptions) -> {
            HttpRequestOptions expectedOptions = new HttpRequestOptions(
                copyFactoryApiUrl + "/users/current/configuration/accounts/" + 
                    "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef", Method.DELETE);
            expectedOptions.getHeaders().put("auth-token", "header.payload.sign");
            assertThat(actualOptions).usingRecursiveComparison().isEqualTo(expectedOptions);
            return CompletableFuture.completedFuture(null);
        });
        copyFactoryClient.removeAccount("0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef").get();
    }
    
    /**
     * Tests {@link ConfigurationClient#removeAccount(String)}
     */
    @Test
    void testDoesNotRemoveCopyFactoryAccountViaApiWithAccountToken() throws Exception {
        copyFactoryClient = new ConfigurationClient(httpClient, "token");
        try {
            copyFactoryClient.removeAccount("id").get();
        } catch (ExecutionException e) {
            assertEquals(
                "You can not invoke removeAccount method, because you have connected with account access token. "
                + "Please use API access token from https://app.metaapi.cloud/token page to invoke this method.",
                e.getCause().getMessage()
            );
        };
    }
    
    /**
     * Tests {@link ConfigurationClient#generateStrategyId()}
     */
    @Test
    void testGeneratesStrategyId() throws Exception {
        StrategyId expectedId = new StrategyId() {{ id = "ABCD"; }};
        httpClient.setRequestMock((actualOptions) -> {
            try {
                HttpRequestOptions expectedOptions = new HttpRequestOptions(
                    copyFactoryApiUrl + "/users/current/configuration/unused-strategy-id", Method.GET);
                expectedOptions.getHeaders().put("auth-token", "header.payload.sign");
                assertThat(actualOptions).usingRecursiveComparison().isEqualTo(expectedOptions);
                return CompletableFuture.completedFuture(jsonMapper.writeValueAsString(expectedId));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        });
        StrategyId actualId = copyFactoryClient.generateStrategyId().get();
        assertThat(actualId).usingRecursiveComparison().isEqualTo(expectedId);
    }
    
    /**
     * Tests {@link ConfigurationClient#generateStrategyId()}
     */
    @Test
    void testDoesNotGeneratesStrategyIdWithAccountToken() throws Exception {
        copyFactoryClient = new ConfigurationClient(httpClient, "token");
        try {
            copyFactoryClient.generateStrategyId().get();
        } catch (ExecutionException e) {
            assertEquals(
                "You can not invoke generateStrategyId method, because you have connected with account access token. "
                + "Please use API access token from https://app.metaapi.cloud/token page to invoke this method.",
                e.getCause().getMessage()
            );
        };
    }
    
    /**
     * Tests {@link ConfigurationClient#updateStrategy(String, CopyFactoryStrategyUpdate)}
     */
    @Test
    void testUpdatesStrategyViaApi() throws Exception {
        httpClient.setRequestMock((actualOptions) -> {
            HttpRequestOptions expectedOptions = new HttpRequestOptions(
                copyFactoryApiUrl + "/users/current/configuration/strategies/ABCD", Method.PUT);
            expectedOptions.getHeaders().put("auth-token", "header.payload.sign");
            expectedOptions.setBody(new CopyFactoryStrategyUpdate() {{
                name = "Test strategy";
                positionLifecycle = "hedging";
                connectionId = "e8867baa-5ec2-45ae-9930-4d5cea18d0d6";
                maxTradeRisk = 0.1;
                stopOutRisk = new CopyFactoryStrategyStopOutRisk() {{
                    value = 0.4;
                    startTime = new IsoTime("2020-08-24T00:00:00.000Z");
                }};
                timeSettings = new CopyFactoryStrategyTimeSettings() {{
                    lifetimeInHours = 192;
                    openingIntervalInMinutes = 5;
                }};
            }});
            assertThat(actualOptions).usingRecursiveComparison().isEqualTo(expectedOptions);
            return CompletableFuture.completedFuture(null);
        });
        copyFactoryClient.updateStrategy("ABCD", new CopyFactoryStrategyUpdate() {{
            name = "Test strategy";
            positionLifecycle = "hedging";
            connectionId = "e8867baa-5ec2-45ae-9930-4d5cea18d0d6";
            maxTradeRisk = 0.1;
            stopOutRisk = new CopyFactoryStrategyStopOutRisk() {{
                value = 0.4;
                startTime = new IsoTime("2020-08-24T00:00:00.000Z");
            }};
            timeSettings = new CopyFactoryStrategyTimeSettings() {{
                lifetimeInHours = 192;
                openingIntervalInMinutes = 5;
            }};
        }}).get();
    }
    
    /**
     * Tests {@link ConfigurationClient#updateStrategy(String, CopyFactoryStrategyUpdate)}
     */
    @Test
    void testDoesNotUpdatesStrategyViaApiWithAccountToken() throws Exception {
        copyFactoryClient = new ConfigurationClient(httpClient, "token");
        try {
            copyFactoryClient.updateStrategy("ABCD", new CopyFactoryStrategyUpdate() {{}}).get();
        } catch (ExecutionException e) {
            assertEquals(
                "You can not invoke updateStrategy method, because you have connected with account access token. "
                + "Please use API access token from https://app.metaapi.cloud/token page to invoke this method.",
                e.getCause().getMessage()
            );
        };
    }
    
    /**
     * Tests {@link ConfigurationClient#getStrategies()}
     */
    @Test
    void testRetrieveStrategiesFromApi() throws Exception {
        List<CopyFactoryStrategy> expectedStrategies = Lists.list(new CopyFactoryStrategy() {{
            _id = "ABCD";
            platformCommissionRate = 0.01;
            name = "Test strategy";
            positionLifecycle = "hedging";
            connectionId = "e8867baa-5ec2-45ae-9930-4d5cea18d0d6";
            maxTradeRisk = 0.1;
            stopOutRisk = new CopyFactoryStrategyStopOutRisk() {{
                value = 0.4;
                startTime = new IsoTime("2020-08-24T00:00:00.000Z");
            }};
            timeSettings = new CopyFactoryStrategyTimeSettings() {{
                lifetimeInHours = 192;
                openingIntervalInMinutes = 5;
            }};
        }});
        httpClient.setRequestMock((actualOptions) -> {
            try {
                HttpRequestOptions expectedOptions = new HttpRequestOptions(
                    copyFactoryApiUrl + "/users/current/configuration/strategies", Method.GET);
                expectedOptions.getHeaders().put("auth-token", "header.payload.sign");
                assertThat(actualOptions).usingRecursiveComparison().isEqualTo(expectedOptions);
                return CompletableFuture.completedFuture(jsonMapper.writeValueAsString(expectedStrategies));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        });
        List<CopyFactoryStrategy> actualStrategies = copyFactoryClient.getStrategies().get();
        assertThat(actualStrategies).usingRecursiveComparison().isEqualTo(expectedStrategies);
    }
    
    /**
     * Tests {@link ConfigurationClient#getStrategies()}
     */
    @Test
    void testDoesNotRetrieveStrategiesFromApiWithAccountToken() throws Exception {
        copyFactoryClient = new ConfigurationClient(httpClient, "token");
        try {
            copyFactoryClient.getStrategies().get();
        } catch (ExecutionException e) {
            assertEquals(
                "You can not invoke getStrategies method, because you have connected with account access token. "
                + "Please use API access token from https://app.metaapi.cloud/token page to invoke this method.",
                e.getCause().getMessage()
            );
        };
    }
    
    /**
     * Tests {@link ConfigurationClient#removeStrategy(String)}
     */
    @Test
    void testRemovesStrategyViaApi() throws Exception {
        httpClient.setRequestMock((actualOptions) -> {
            HttpRequestOptions expectedOptions = new HttpRequestOptions(
                copyFactoryApiUrl + "/users/current/configuration/strategies/ABCD", Method.DELETE);
            expectedOptions.getHeaders().put("auth-token", "header.payload.sign");
            assertThat(actualOptions).usingRecursiveComparison().isEqualTo(expectedOptions);
            return CompletableFuture.completedFuture(null);
        });
        copyFactoryClient.removeStrategy("ABCD").get();
    }
    
    /**
     * Tests {@link ConfigurationClient#removeStrategy(String)}
     */
    @Test
    void testDoesNotRemoveStrategyViaApiWithAccountToken() throws Exception {
        copyFactoryClient = new ConfigurationClient(httpClient, "token");
        try {
            copyFactoryClient.removeStrategy("id").get();
        } catch (ExecutionException e) {
            assertEquals(
                "You can not invoke removeStrategy method, because you have connected with account access token. "
                + "Please use API access token from https://app.metaapi.cloud/token page to invoke this method.",
                e.getCause().getMessage()
            );
        };
    }
    
    /**
     * Tests {@link ConfigurationClient#getPortfolioStrategies()}
     */
    @Test
    void testRetrievePortfolioStrategiesFromApi() throws Exception {
        List<CopyFactoryPortfolioStrategy> expectedStrategies = Lists.list(new CopyFactoryPortfolioStrategy() {{
            _id = "ABCD";
            platformCommissionRate = 0.01;
            name = "Test strategy";
            members = Lists.list(new CopyFactoryPortfolioMember() {{
                strategyId = "BCDE";
            }});
        }});
        httpClient.setRequestMock((actualOptions) -> {
            try {
                HttpRequestOptions expectedOptions = new HttpRequestOptions(
                    copyFactoryApiUrl + "/users/current/configuration/portfolio-strategies", Method.GET);
                expectedOptions.getHeaders().put("auth-token", "header.payload.sign");
                assertThat(actualOptions).usingRecursiveComparison().isEqualTo(expectedOptions);
                return CompletableFuture.completedFuture(jsonMapper.writeValueAsString(expectedStrategies));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        });
        List<CopyFactoryPortfolioStrategy> actualStrategies = copyFactoryClient.getPortfolioStrategies().get();
        assertThat(actualStrategies).usingRecursiveComparison().isEqualTo(expectedStrategies);
    }
    
    /**
     * Tests {@link ConfigurationClient#getPortfolioStrategies()}
     */
    @Test
    void testDoesNotRetrievePortfolioStrategiesFromApiWithAccountToken() throws Exception {
        copyFactoryClient = new ConfigurationClient(httpClient, "token");
        try {
            copyFactoryClient.getPortfolioStrategies().get();
        } catch (ExecutionException e) {
            assertEquals(
                "You can not invoke getPortfolioStrategies method, because you have connected with account access token. "
                + "Please use API access token from https://app.metaapi.cloud/token page to invoke this method.",
                e.getCause().getMessage()
            );
        };
    }
    
    /**
     * Tests {@link ConfigurationClient#updatePortfolioStrategy(String, CopyFactoryPortfolioStrategyUpdate)}
     */
    @Test
    void testUpdatesPortfolioStrategyViaApi() throws Exception {
        httpClient.setRequestMock((actualOptions) -> {
            HttpRequestOptions expectedOptions = new HttpRequestOptions(
                copyFactoryApiUrl + "/users/current/configuration/portfolio-strategies/ABCD", Method.PUT);
            expectedOptions.getHeaders().put("auth-token", "header.payload.sign");
            expectedOptions.setBody(new CopyFactoryPortfolioStrategyUpdate() {{
                name = "Test strategy";
                members = Lists.list(new CopyFactoryPortfolioMember() {{
                    strategyId = "BCDE";
                }});
            }});
            assertThat(actualOptions).usingRecursiveComparison().isEqualTo(expectedOptions);
            return CompletableFuture.completedFuture(null);
        });
        copyFactoryClient.updatePortfolioStrategy("ABCD", new CopyFactoryPortfolioStrategyUpdate() {{
            name = "Test strategy";
            members = Lists.list(new CopyFactoryPortfolioMember() {{
                strategyId = "BCDE";
            }});
        }}).get();
    }
    
    /**
     * Tests {@link ConfigurationClient#updatePortfolioStrategy(String, CopyFactoryPortfolioStrategyUpdate)}
     */
    @Test
    void testDoesNotUpdatesPortfolioStrategyViaApiWithAccountToken() throws Exception {
        copyFactoryClient = new ConfigurationClient(httpClient, "token");
        try {
            copyFactoryClient.updatePortfolioStrategy("ABCD", new CopyFactoryPortfolioStrategyUpdate() {{}}).get();
        } catch (ExecutionException e) {
            assertEquals(
                "You can not invoke updatePortfolioStrategy method, because you have connected with account access token. "
                + "Please use API access token from https://app.metaapi.cloud/token page to invoke this method.",
                e.getCause().getMessage()
            );
        };
    }
    
    /**
     * Tests {@link ConfigurationClient#removePortfolioStrategy(String)}
     */
    @Test
    void testRemovesPortfolioStrategyViaApi() throws Exception {
        httpClient.setRequestMock((actualOptions) -> {
            HttpRequestOptions expectedOptions = new HttpRequestOptions(
                copyFactoryApiUrl + "/users/current/configuration/portfolio-strategies/ABCD", Method.DELETE);
            expectedOptions.getHeaders().put("auth-token", "header.payload.sign");
            assertThat(actualOptions).usingRecursiveComparison().isEqualTo(expectedOptions);
            return CompletableFuture.completedFuture(null);
        });
        copyFactoryClient.removePortfolioStrategy("ABCD").get();
    }
    
    /**
     * Tests {@link ConfigurationClient#removePortfolioStrategy(String)}
     */
    @Test
    void testDoesNotRemovePortfolioStrategyViaApiWithAccountToken() throws Exception {
        copyFactoryClient = new ConfigurationClient(httpClient, "token");
        try {
            copyFactoryClient.removePortfolioStrategy("id").get();
        } catch (ExecutionException e) {
            assertEquals(
                "You can not invoke removePortfolioStrategy method, because you have connected with account access token. "
                + "Please use API access token from https://app.metaapi.cloud/token page to invoke this method.",
                e.getCause().getMessage()
            );
        };
    }
    
    /**
     * Tests {@link ConfigurationClient#getActiveResynchronizationTasks(String)}
     */
    @Test
    void testRetrievesActiveResynchronizationTasksFromApi() {
        List<ResynchronizationTask> expectedTasks = Lists.list(new ResynchronizationTask() {{
            _id = "ABCD";
            type = TaskType.CREATE_STRATEGY;
            createdAt = new IsoTime("2020-08-25T00:00:00.000Z");
            status = TaskStatus.EXECUTING;
        }});
        httpClient.setRequestMock((actualOptions) -> {
            try {
                HttpRequestOptions expectedOptions = new HttpRequestOptions(
                    copyFactoryApiUrl + "/users/current/configuration/connections/" + 
                        "accountId/active-resynchronization-tasks", Method.GET);
                expectedOptions.getHeaders().put("auth-token", "header.payload.sign");
                assertThat(actualOptions).usingRecursiveComparison().isEqualTo(expectedOptions);
                return CompletableFuture.completedFuture(jsonMapper.writeValueAsString(expectedTasks));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        });
        List<ResynchronizationTask> actualTasks = copyFactoryClient.getActiveResynchronizationTasks("accountId").join();
        assertThat(actualTasks).usingRecursiveComparison().isEqualTo(expectedTasks);
    }
    
    /**
     * Tests {@link ConfigurationClient#getActiveResynchronizationTasks(String)}
     */
    @Test
    void testDoesNotRetrieveActiveResynchronizationTasksFromApiWithAccountToken() throws Exception {
        copyFactoryClient = new ConfigurationClient(httpClient, "token");
        try {
            copyFactoryClient.getActiveResynchronizationTasks("accountId").get();
        } catch (ExecutionException e) {
            assertEquals(
                "You can not invoke getActiveResynchronizationTasks method, because you have connected with account access token. "
                + "Please use API access token from https://app.metaapi.cloud/token page to invoke this method.",
                e.getCause().getMessage()
            );
        };
    }
    
    /**
     * Tests {@link ConfigurationClient#waitResynchronizationTasksCompleted(String, Integer, Integer)}
     */
    @Test
    void waitsUntilActiveResynchronizationTasksAreCompleted() throws Exception {
        List<ResynchronizationTask> activeTasks = Lists.list(new ResynchronizationTask() {{
            _id = "ABCD";
            type = TaskType.CREATE_STRATEGY;
            createdAt = new IsoTime("2020-08-25T00:00:00.000Z");
            status = TaskStatus.EXECUTING;
        }});
        httpClient = Mockito.mock(HttpClientMock.class);
        Mockito.when(httpClient.requestJson(Mockito.any(HttpRequestOptions.class), Mockito.any()))
            .thenReturn(CompletableFuture.completedFuture(activeTasks.toArray(new ResynchronizationTask[0])))
            .thenReturn(CompletableFuture.completedFuture(activeTasks.toArray(new ResynchronizationTask[0])))
            .thenReturn(CompletableFuture.completedFuture(Lists.emptyList().toArray(new ResynchronizationTask[0])));
        copyFactoryClient = new ConfigurationClient(httpClient, "header.payload.sign");
        copyFactoryClient.waitResynchronizationTasksCompleted("accountId", 2, 50).join();
        Mockito.verify(httpClient, Mockito.times(3)).requestJson(Mockito.argThat(arg -> {
            HttpRequestOptions expected = new HttpRequestOptions(copyFactoryApiUrl
                + "/users/current/configuration/connections/accountId/active-resynchronization-tasks", Method.GET);
            expected.getHeaders().put("auth-token", "header.payload.sign");
            assertThat(arg).usingRecursiveComparison().isEqualTo(expected);
            return true;
        }), Mockito.any());
    }
    
    /**
     * Tests {@link ConfigurationClient#waitResynchronizationTasksCompleted(String, Integer, Integer)}
     */
    @Test
    void waitsTimesOutWaitingForActiveResynchronizationTasksAreCompleted() throws Exception {
        List<ResynchronizationTask> activeTasks = Lists.list(new ResynchronizationTask() {{
            _id = "ABCD";
            type = TaskType.CREATE_STRATEGY;
            createdAt = new IsoTime("2020-08-25T00:00:00.000Z");
            status = TaskStatus.EXECUTING;
        }});
        httpClient = Mockito.mock(HttpClientMock.class);
        Mockito.when(httpClient.requestJson(Mockito.any(HttpRequestOptions.class), Mockito.any()))
            .thenReturn(CompletableFuture.completedFuture(activeTasks.toArray(new ResynchronizationTask[0])));
        copyFactoryClient = new ConfigurationClient(httpClient, "header.payload.sign");
        assertThrows(TimeoutException.class, () -> {
            try {
                copyFactoryClient.waitResynchronizationTasksCompleted("accountId", 1, 50).join();
                throw new Exception("TimeoutException is expected");
            } catch (CompletionException e) {
                throw e.getCause();
            }
        });
        Mockito.verify(httpClient, Mockito.atLeastOnce()).requestJson(Mockito.argThat(arg -> {
            HttpRequestOptions expected = new HttpRequestOptions(copyFactoryApiUrl
                + "/users/current/configuration/connections/accountId/active-resynchronization-tasks", Method.GET);
            expected.getHeaders().put("auth-token", "header.payload.sign");
            assertThat(arg).usingRecursiveComparison().isEqualTo(expected);
            return true;
        }), Mockito.any());
    }
}