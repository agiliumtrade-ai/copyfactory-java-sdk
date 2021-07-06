package cloud.metaapi.sdk.clients;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cloud.metaapi.sdk.clients.HttpRequestOptions.Method;
import cloud.metaapi.sdk.clients.error_handler.*;
import cloud.metaapi.sdk.clients.mocks.HttpClientMock;
import cloud.metaapi.sdk.clients.models.IsoTime;
import cloud.metaapi.sdk.util.JsonMapper;
import kong.unirest.Headers;
import kong.unirest.HttpResponse;
import kong.unirest.UnirestException;

import java.net.SocketTimeoutException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
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
  private HttpRequestOptions requestOpts = new HttpRequestOptions("http://metaapi.cloud", Method.GET);
  
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
    assertTimeoutPreemptively(Duration.ofSeconds(10), () -> {
      try {
        httpClient.request(requestOpts).get();
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
  public void testRetriesRequestOnFailWithApiError() {
    CompletableFuture<HttpResponse<String>> httpErrorResponse = new CompletableFuture<>();
    httpErrorResponse.completeExceptionally(new Exception("test"));
    HttpResponse<String> httpOkResponse = getHttpOkResponse();
    httpClient = Mockito.spy(HttpClient.class);
    Mockito.when(httpClient.makeRequest(Mockito.any()))
      .thenReturn(httpErrorResponse).thenReturn(httpErrorResponse)
      .thenReturn(CompletableFuture.completedFuture(httpOkResponse));
    String body = httpClient.request(requestOpts).join();
    assertEquals("response", body);
  }
  
  /**
   * Tests {@link HttpClient#request(HttpRequestOptions)}
   */
  @Test
  @SuppressWarnings("unchecked")
  public void testRetriesRequestOnFailWithInternalError() {
    HttpResponse<String> httpErrorResponse = (HttpResponse<String>) Mockito.mock(HttpResponse.class);
    Mockito.when(httpErrorResponse.getStatus()).thenReturn(500);
    Mockito.when(httpErrorResponse.getBody()).thenReturn("Internal error");
    HttpResponse<String> httpOkResponse = getHttpOkResponse();
    httpClient = Mockito.spy(HttpClient.class);
    Mockito.when(httpClient.makeRequest(Mockito.any()))
      .thenReturn(CompletableFuture.completedFuture(httpErrorResponse))
      .thenReturn(CompletableFuture.completedFuture(httpErrorResponse))
      .thenReturn(CompletableFuture.completedFuture(httpOkResponse));
    String body = httpClient.request(requestOpts).join();
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
        httpClient.request(requestOpts).join();
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
  public void testDoesNotRetryIfErrorIsNeitherInternalErrorNorApiError() {
    HttpResponse<String> httpErrorResponse = (HttpResponse<String>) Mockito.mock(HttpResponse.class);
    Mockito.when(httpErrorResponse.getStatus()).thenReturn(400);
    Mockito.when(httpErrorResponse.getBody()).thenReturn("{\"message\": \"test\"}");
    HttpResponse<String> httpOkResponse = getHttpOkResponse();
    httpClient = Mockito.spy(HttpClient.class);
    Mockito.when(httpClient.makeRequest(Mockito.any()))
      .thenReturn(CompletableFuture.completedFuture(httpErrorResponse))
      .thenReturn(CompletableFuture.completedFuture(httpErrorResponse))
      .thenReturn(CompletableFuture.completedFuture(httpOkResponse));
    assertThrows(CompletionException.class, () -> {
      try {
        httpClient.request(requestOpts).join();
      } catch (CompletionException e) {
        assertTrue(e.getCause() instanceof ValidationException);
        assertEquals("test", e.getCause().getMessage());
        throw e;
      }
    });
    Mockito.verify(httpClient, Mockito.times(1)).makeRequest(Mockito.any());
  }
  
  @SuppressWarnings("unchecked")
  HttpResponse<String> getTooManyRequestsError(int sec) {
    HttpResponse<String> httpResponse = (HttpResponse<String>) Mockito.mock(HttpResponse.class);
    ObjectNode error = JsonMapper.getInstance().createObjectNode();
    error.put("error", "TooManyRequestsError");
    error.put("message", "test");
    ObjectNode metadata = JsonMapper.getInstance().createObjectNode();
    metadata.set("recommendedRetryTime", JsonMapper.getInstance()
      .valueToTree(new IsoTime(Date.from(Instant.now().plusSeconds(sec)))));
    error.set("metadata", metadata);
    Mockito.when(httpResponse.getStatus()).thenReturn(429);
    Mockito.when(httpResponse.getBody()).thenReturn(error.toString());
    return httpResponse;
  }
  
  @SuppressWarnings("unchecked")
  HttpResponse<String> getHttpOkResponse() {
    HttpResponse<String> httpOkResponse = (HttpResponse<String>) Mockito.mock(HttpResponse.class);
    Mockito.when(httpOkResponse.getStatus()).thenReturn(200);
    Mockito.when(httpOkResponse.getBody()).thenReturn("response");
    return httpOkResponse;
  }
  
  /**
   * Tests {@link HttpClient#request(HttpRequestOptions)}
   */
  @Test
  public void testRetriesRequestAfterWaitingOnFailWithTooManyRequestsError() {
    HttpResponse<String> httpErrorResponse1 = getTooManyRequestsError(2);
    HttpResponse<String> httpErrorResponse2 = getTooManyRequestsError(3);
    HttpResponse<String> httpOkResponse = getHttpOkResponse();
    httpClient = Mockito.spy(HttpClient.class);
    Mockito.when(httpClient.makeRequest(Mockito.any()))
      .thenReturn(CompletableFuture.completedFuture(httpErrorResponse1))
      .thenReturn(CompletableFuture.completedFuture(httpErrorResponse2))
      .thenReturn(CompletableFuture.completedFuture(httpOkResponse));
    String body = httpClient.request(requestOpts).join();
    assertEquals("response", body);
    Mockito.verify(httpClient, Mockito.times(3)).makeRequest(Mockito.any());
  }
  
  /**
   * Tests {@link HttpClient#request(HttpRequestOptions)}
   */
  @Test
  public void testReturnsErrorIfRecommendedRetryTimeIsTooLong() {
    HttpResponse<String> httpErrorResponse1 = getTooManyRequestsError(2);
    HttpResponse<String> httpErrorResponse2 = getTooManyRequestsError(300);
    HttpResponse<String> httpOkResponse = getHttpOkResponse();
    httpClient = Mockito.spy(HttpClient.class);
    Mockito.when(httpClient.makeRequest(Mockito.any()))
      .thenReturn(CompletableFuture.completedFuture(httpErrorResponse1))
      .thenReturn(CompletableFuture.completedFuture(httpErrorResponse2))
      .thenReturn(CompletableFuture.completedFuture(httpOkResponse));
    try {
      String response = httpClient.request(requestOpts).join();
      assertNull(response);
    } catch (Throwable err) {
      assertTrue(err.getCause() instanceof TooManyRequestsException);
      assertEquals("test", err.getCause().getMessage());
      
    }
    Mockito.verify(httpClient, Mockito.times(2)).makeRequest(Mockito.any());
  }
  
  /**
   * Tests {@link HttpClient#request(HttpRequestOptions)}
   */
  @Test
  public void testDoesNotCountsRetryingTooManyRequestsError() {
    HttpResponse<String> httpErrorResponse1 = getTooManyRequestsError(1);
    CompletableFuture<HttpResponse<String>> httpErrorResponse2 = new CompletableFuture<>();
    httpErrorResponse2.completeExceptionally(new Exception("test"));
    HttpResponse<String> httpOkResponse = getHttpOkResponse();
    httpClient = Mockito.spy(new HttpClient(60000, 60000, new RetryOptions() {{ retries = 1; }}));
    Mockito.when(httpClient.makeRequest(Mockito.any()))
      .thenReturn(CompletableFuture.completedFuture(httpErrorResponse1))
      .thenReturn(httpErrorResponse2)
      .thenReturn(CompletableFuture.completedFuture(httpOkResponse));
    String body = httpClient.request(requestOpts).join();
    assertEquals("response", body);
    Mockito.verify(httpClient, Mockito.times(3)).makeRequest(Mockito.any());
  }
  
  @SuppressWarnings("unchecked")
  HttpResponse<String> getHttpRetryResponse(int seconds) {
    Headers headers = Mockito.mock(Headers.class);
    Mockito.when(headers.getFirst("retry-after")).thenReturn(String.valueOf(seconds));
    HttpResponse<String> response = (HttpResponse<String>) Mockito.mock(HttpResponse.class);
    Mockito.when(response.getStatus()).thenReturn(202);
    Mockito.when(response.getHeaders()).thenReturn(headers);
    return response;
  }
  
  /**
   * Tests {@link HttpClient#request(HttpRequestOptions)}
   */
  @Test
  public void testWaitsForTheRetryAfterHeaderTimeBeforeRetrying() {
    HttpResponse<String> retryResponse = getHttpRetryResponse(3);
    HttpResponse<String> httpOkResponse = getHttpOkResponse();
    httpClient = Mockito.spy(new HttpClient(60000, 60000, new RetryOptions() {{ retries = 1; }}));
    Mockito.when(httpClient.makeRequest(Mockito.any()))
      .thenReturn(CompletableFuture.completedFuture(retryResponse))
      .thenReturn(CompletableFuture.completedFuture(retryResponse))
      .thenReturn(CompletableFuture.completedFuture(httpOkResponse));
    String body = httpClient.request(requestOpts).join();
    assertEquals("response", body);
    Mockito.verify(httpClient, Mockito.times(3)).makeRequest(Mockito.any());
  }
  
  /**
   * Tests {@link HttpClient#request(HttpRequestOptions)}
   */
  @Test
  public void testReturnsTimeoutErrorIfRetryAfterHeaderTimeIsTooLong() {
    HttpResponse<String> retryResponse = getHttpRetryResponse(30);
    httpClient = Mockito.spy(new HttpClient(60000, 60000, new RetryOptions() {{ maxDelayInSeconds = 3; }}));
    Mockito.when(httpClient.makeRequest(Mockito.any())).thenReturn(CompletableFuture.completedFuture(retryResponse));
    try {
      httpClient.request(requestOpts).join();
      fail();
    } catch (Throwable err) {
      assertTrue(err.getCause() instanceof TimeoutException);
      assertEquals("Timed out waiting for the end of the process of calculating metrics", err.getCause().getMessage());
    }
    Mockito.verify(httpClient, Mockito.times(1)).makeRequest(Mockito.any());
  }
  
  /**
   * Tests {@link HttpClient#request(HttpRequestOptions)}
   */
  @Test
  public void testReturnsTimeoutErrorIfTimedOutToRetry() {
    HttpResponse<String> retryResponse = getHttpRetryResponse(1);
    httpClient = Mockito.spy(new HttpClient(60000, 60000, new RetryOptions() {{ maxDelayInSeconds = 2; retries = 3; }}));
    Mockito.when(httpClient.makeRequest(Mockito.any())).thenReturn(CompletableFuture.completedFuture(retryResponse));
    try {
      httpClient.request(requestOpts).join();
      fail();
    } catch (Throwable err) {
      assertTrue(err.getCause() instanceof TimeoutException);
      assertEquals("Timed out waiting for the end of the process of calculating metrics", err.getCause().getMessage());
    }
    Mockito.verify(httpClient, Mockito.times(6)).makeRequest(Mockito.any());
  }
}