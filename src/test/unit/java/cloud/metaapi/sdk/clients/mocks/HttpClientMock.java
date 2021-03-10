package cloud.metaapi.sdk.clients.mocks;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import cloud.metaapi.sdk.clients.HttpClient;
import cloud.metaapi.sdk.clients.HttpRequestOptions;
import cloud.metaapi.sdk.clients.RetryOptions;

/**
 * HTTP client service mock for tests
 */
public class HttpClientMock extends HttpClient {
  
  private Function<HttpRequestOptions, CompletableFuture<String>> requestMock;
  
  /**
   * Constructs HTTP client mock
   * @param requestMock mocked request function which must return CompletableFuture with response body string
   */
  public HttpClientMock(Function<HttpRequestOptions, CompletableFuture<String>> requestMock) {
    this(requestMock, 60000, 60000);
  }
  
  /**
   * Constructs HTTP client mock
   * @param requestMock mocked request function which must return CompletableFuture with response body string
   * @param requestTimeout request timeout in milliseconds
   * @param connectTimeout connect timeout in milliseconds
   */
  public HttpClientMock(Function<HttpRequestOptions, CompletableFuture<String>> requestMock, int requestTimeout,
    int connectTimeout) {
    this(requestMock, requestTimeout, connectTimeout, new RetryOptions());
  }
  
  /**
   * Constructs HTTP client mock
   * @param requestMock mocked request function which must return CompletableFuture with response body string
   * @param requestTimeout request timeout in milliseconds
   * @param connectTimeout connect timeout in milliseconds
   * @param retryOpts retry options
   */
  public HttpClientMock(Function<HttpRequestOptions, CompletableFuture<String>> requestMock, int requestTimeout,
    int connectTimeout, RetryOptions retryOpts) {
    super(requestTimeout, connectTimeout, retryOpts);
    this.requestMock = requestMock;
  }
  
  /**
   * Overridden request method of HttpClient with replaced implementation that uses mocked request function
   * @param options request options
   */
  @Override
  public CompletableFuture<String> request(HttpRequestOptions options) {
    return requestMock.apply(options);
  }
  
  /**
   * Overridden request method of HttpClient with replaced implementation that uses mocked request function
   * @param options request options
   * @param retryCounter retry counter
   */
  @Override
  public CompletableFuture<String> request(HttpRequestOptions options, int retryCounter) {
    return requestMock.apply(options);
  }
  
  /**
   * Sets request mock function
   * @param mock mocked request function which must return CompletableFuture with response body string
   */
  public void setRequestMock(Function<HttpRequestOptions, CompletableFuture<String>> mock) {
    this.requestMock = mock;
  }
}
