package cloud.metaapi.sdk.clients;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonProcessingException;

import cloud.metaapi.sdk.clients.HttpRequestOptions.Method;
import cloud.metaapi.sdk.clients.error_handler.*;
import cloud.metaapi.sdk.clients.mocks.HttpClientMock;
import cloud.metaapi.sdk.clients.models.IsoTime;
import kong.unirest.HttpResponse;
import kong.unirest.UnirestException;

import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Class for testing json requests
 */
class JsonModelExample {
  /**
   * Some first number
   */
  public int a;
  /**
   * Some second number
   */
  public int b;
}

/**
 * Tests {@link HttpClient}
 */
public class HttpClientTest {

  private HttpClient httpClient;
  
  @BeforeEach
  public void setUp() {
    httpClient = new HttpClient();
  }
  
  /**
   * Tests {@link HttpClient#request(HttpRequestOptions)}
   */
  @Test
  public void testLoadsHtmlPageFromExampleCom() throws Exception {
    HttpRequestOptions opts = new HttpRequestOptions("http://example.com", Method.GET);
    String response = httpClient.request(opts).get(10000, TimeUnit.MILLISECONDS);
    assertNotEquals(-1, response.indexOf("doctype html"));
  }
  
  /**
   * Tests {@link HttpClient#request(HttpRequestOptions)}
   */
  @Test
  public void testReturnsNotFoundIfServerReturns404() {
    assertThrows(NotFoundException.class, () -> {
      HttpRequestOptions opts = new HttpRequestOptions("http://example.com/not-found", Method.GET);
      try {
        httpClient.request(opts).get(10000, TimeUnit.MILLISECONDS);
      } catch (ExecutionException e) {
        throw e.getCause();
      }
    });
  }
  
  /**
   * Tests {@link HttpClient#requestJson(HttpRequestOptions, Class)}
   */
  @Test
  public void testCanReturnJsonInResponse() throws Exception {
    JsonModelExample expected = new JsonModelExample();
    expected.a = 42;
    expected.b = 28;
    HttpClient clientMock = new HttpClientMock((opts) -> {
      return CompletableFuture.completedFuture("{\"a\": 42, \"b\": \"28\"}");
    });
    JsonModelExample actual = clientMock.requestJson(null, JsonModelExample.class).get();
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }
  
  /**
   * Tests {@link HttpClient#requestJson(HttpRequestOptions, Class)}
   */
  @Test
  public void testCompletesExceptionallyDuringParsingInvalidJsonString() {
    assertThrows(JsonProcessingException.class, () -> {
      String invalidJsonString = "{a: 42, b: 28}";
      HttpClient clientMock = new HttpClientMock((opts) -> CompletableFuture.completedFuture(invalidJsonString));
      try {
        clientMock.requestJson(null, JsonModelExample.class).get();
      } catch (ExecutionException e) {
        throw e.getCause();
      }
    });
  }
  
  /**
   * Tests {@link HttpClient#request(HttpRequestOptions)}
   */
  @Test
  public void testReturnsTimeoutExceptionIfRequestIsTimedOut() {
    httpClient = new HttpClient(1, 60000, new RetryOptions() {{ retries = 2; }});
    HttpRequestOptions opts = new HttpRequestOptions("http://metaapi.cloud", Method.GET);
    assertTimeoutPreemptively(Duration.ofSeconds(10), () -> {
      try {
        httpClient.request(opts).get();
      } catch (ExecutionException e) {
        assertTrue(e.getCause() instanceof ApiException);
        assertTrue(e.getCause().getCause() instanceof UnirestException);
        assertTrue(e.getCause().getCause().getCause() instanceof SocketTimeoutException);
      }
    });
  }
  
  /**
   * Tests {@link HttpClient#request(HttpRequestOptions)}
   */
  @Test
  @SuppressWarnings("unchecked")
  public void testRetriesRequestOnFail() {
    CompletableFuture<HttpResponse<String>> httpErrorResponse = new CompletableFuture<>();
    httpErrorResponse.completeExceptionally(new UnirestException(new SocketTimeoutException()));
    HttpResponse<String> httpOkResponse = (HttpResponse<String>) Mockito.mock(HttpResponse.class);
    Mockito.when(httpOkResponse.getStatus()).thenReturn(200);
    Mockito.when(httpOkResponse.getBody()).thenReturn("response");
    httpClient = Mockito.spy(HttpClient.class);
    Mockito.when(httpClient.makeRequest(Mockito.any()))
      .thenReturn(httpErrorResponse).thenReturn(httpErrorResponse)
      .thenReturn(CompletableFuture.completedFuture(httpOkResponse));
    String body = httpClient.request(new HttpRequestOptions("http://metaapi.cloud", Method.GET)).join();
    assertEquals("response", body);
  }
  
  /**
   * Tests {@link HttpClient#request(HttpRequestOptions)}
   */
  @Test
  @SuppressWarnings("unchecked")
  public void testReturnsErrorIfRetryLimitIsExceeded() {
    HttpResponse<String> httpResponse = (HttpResponse<String>) Mockito.mock(HttpResponse.class);
    Mockito.when(httpResponse.getStatus()).thenReturn(502);
    Mockito.when(httpResponse.getBody()).thenReturn("{\"message\": \"test\"}");
    httpClient = Mockito.spy(new HttpClient(60000, 60000, new RetryOptions() {{ retries = 2; }}));
    Mockito.when(httpClient.makeRequest(Mockito.any())).thenReturn(CompletableFuture.completedFuture(httpResponse));
    assertThrows(CompletionException.class, () -> {
      try {
        httpClient.request(new HttpRequestOptions("http://metaapi.cloud", Method.GET)).join();
      } catch (CompletionException e) {
        assertTrue(e.getCause() instanceof ApiException);
        assertEquals("test", e.getCause().getMessage());
        throw e;
      }
    });
  }
  
  /**
   * Tests {@link HttpClient#request(HttpRequestOptions)}
   */
  @Test
  @SuppressWarnings("unchecked")
  public void testDoesNotRetryIfErrorNotSpecified() {
    HttpResponse<String> httpResponse = (HttpResponse<String>) Mockito.mock(HttpResponse.class);
    Mockito.when(httpResponse.getStatus()).thenReturn(400);
    Mockito.when(httpResponse.getBody()).thenReturn("{\"message\": \"test\"}");
    httpClient = Mockito.spy(HttpClient.class);
    Mockito.when(httpClient.makeRequest(Mockito.any())).thenReturn(CompletableFuture.completedFuture(httpResponse));
    assertThrows(CompletionException.class, () -> {
      try {
        httpClient.request(new HttpRequestOptions("http://metaapi.cloud", Method.GET)).join();
      } catch (CompletionException e) {
        assertTrue(e.getCause() instanceof ValidationException);
        assertEquals("test", e.getCause().getMessage());
        throw e;
      }
    });
  }
  
  /**
   * Tests {@link HttpClient#request(HttpRequestOptions)}
   */
  @Test
  @SuppressWarnings("unchecked")
  public void testParsesTooManyRequestsErrorMetadata() {
    HttpResponse<String> httpResponse = (HttpResponse<String>) Mockito.mock(HttpResponse.class);
    Mockito.when(httpResponse.getStatus()).thenReturn(429);
    Mockito.when(httpResponse.getBody()).thenReturn("{\"id\": 1, \"error\": \"TooManyRequestsException\", "
        + "\"message\": \"test\", \"metadata\": {\"periodInMinutes\": 5, \"requestsPerPeriodAllowed\": 10, "
        + "\"recommendedRetryTime\": \"2020-04-15T02:45:06.521Z\"}}");
    httpClient = Mockito.spy(HttpClient.class);
    Mockito.when(httpClient.makeRequest(Mockito.any())).thenReturn(CompletableFuture.completedFuture(httpResponse));
    assertThrows(CompletionException.class, () -> {
      try {
        httpClient.request(new HttpRequestOptions("http://metaapi.cloud", Method.GET)).join();
      } catch (CompletionException e) {
        assertTrue(e.getCause() instanceof TooManyRequestsException);
        TooManyRequestsException tooManyRequestsError = (TooManyRequestsException) e.getCause();
        assertEquals("test", tooManyRequestsError.getMessage());
        assertThat(tooManyRequestsError.metadata).usingRecursiveComparison()
          .isEqualTo(new TooManyRequestsException.TooManyRequestsExceptionMetadata() {{
            periodInMinutes = 5;
            requestsPerPeriodAllowed = 10;
            recommendedRetryTime = new IsoTime("2020-04-15T02:45:06.521Z");
        }});
        throw e;
      }
    });
  }
}