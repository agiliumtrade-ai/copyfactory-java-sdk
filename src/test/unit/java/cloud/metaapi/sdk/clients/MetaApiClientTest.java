package cloud.metaapi.sdk.clients;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cloud.metaapi.sdk.clients.mocks.HttpClientMock;

/**
 * Tests {@link MetaApiClient}
 */
public class MetaApiClientTest {
    
    private static HttpClient httpClient = new HttpClientMock((options) -> CompletableFuture.completedFuture("empty"));
    private MetaApiClient apiClient;
    
    @BeforeEach
    void setUp() {
        apiClient = new MetaApiClient(httpClient, "token");
    }
    
    /**
     * Tests {@link MetaApiClient#getTokenType()}
     */
    @Test
    void testReturnsAccountTokenType() {
        assertTrue(apiClient.getTokenType().equals("account"));
    }
    
    /**
     * Tests {@link MetaApiClient#getTokenType()}
     */
    @Test
    void testReturnsApiTokenType() {
        apiClient = new MetaApiClient(httpClient, "header.payload.sign");
        assertTrue(apiClient.getTokenType().equals("api"));
    }
    
    /**
     * Tests {@link MetaApiClient#isNotJwtToken()}
     */
    @Test
    void testChecksThatCurrentTokenIsNotJwt() {
        assertTrue(apiClient.isNotJwtToken());
    }
    
    /**
     * Tests {@link MetaApiClient#isNotAccountToken()}
     */
    @Test
    void testChecksThatCurrenrTokenIsNotAccountToken() {
        apiClient = new MetaApiClient(httpClient, "header.payload.sign");
        assertTrue(apiClient.isNotAccountToken());
    }
    
    /**
     * Tests {@link MetaApiClient#handleNoAccessError(String)}
     */
    @Test
    void testHandlesNoAccessErrorWithAccountToken() throws Exception {
        try {
            apiClient.handleNoAccessError("methodName").get();
            throw new Exception("MethodAccessException expected");
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            assertTrue(cause instanceof MethodAccessException);
            assertEquals(
                "You can not invoke methodName method, because you have connected with account access token. "
                + "Please use API access token from https://app.metaapi.cloud/token page to invoke this method.",
                cause.getMessage()
            );
        }
    }
    
    /**
     * Tests {@link MetaApiClient#handleNoAccessError(String)}
     */
    @Test
    void testHandlesNoAccessErrorWithApiToken() throws Exception {
        apiClient = new MetaApiClient(httpClient, "header.payload.sign");
        try {
            apiClient.handleNoAccessError("methodName").get();
            throw new Exception("MethodAccessException expected");
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            assertTrue(cause instanceof MethodAccessException);
            assertEquals(
                "You can not invoke methodName method, because you have connected with API access token. "
                + "Please use account access token to invoke this method.",
                cause.getMessage()
            );
        }
    }
}