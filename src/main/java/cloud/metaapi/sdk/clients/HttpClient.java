package cloud.metaapi.sdk.clients;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import com.fasterxml.jackson.core.JsonProcessingException;

import cloud.metaapi.sdk.clients.HttpRequestOptions.FileStreamField;
import cloud.metaapi.sdk.clients.error_handler.*;
import cloud.metaapi.sdk.clients.error_handler.TooManyRequestsException.TooManyRequestsExceptionMetadata;
import cloud.metaapi.sdk.clients.models.Error;
import cloud.metaapi.sdk.util.JsonMapper;
import kong.unirest.HttpRequest;
import kong.unirest.HttpRequestWithBody;
import kong.unirest.HttpResponse;
import kong.unirest.MultipartBody;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

/**
 * HTTP client library based on request-promise
 */
public class HttpClient {
  
  private int requestTimeout;
  private int connectTimeout;
  private int retries;
  private int minRetryDelay;
  private int maxRetryDelay;
  
  /**
   * Constructs HttpClient class instance. Connect and request timeout are {@code 1 minute} each.
   */
  public HttpClient() {
    this(60000, 60000);
  }
  
  /**
   * Constructs HttpClient class instance
   * @param requestTimeout request timeout in milliseconds
   * @param connectTimeout connect timeout in milliseconds
   */
  public HttpClient(int requestTimeout, int connectTimeout) {
    this(requestTimeout, connectTimeout, new RetryOptions());
  }
  
  /**
   * Constructs HttpClient class instance
   * @param requestTimeout request timeout in milliseconds
   * @param connectTimeout connect timeout in milliseconds
   * @param retryOpts retrying options
   */
  public HttpClient(int requestTimeout, int connectTimeout, RetryOptions retryOpts) {
    this.requestTimeout = requestTimeout;
    this.connectTimeout = connectTimeout;
    this.retries = retryOpts.retries;
    this.minRetryDelay = retryOpts.minDelayInSeconds * 1000;
    this.maxRetryDelay = retryOpts.maxDelayInSeconds * 1000;
  }
  
  /**
   * Performs a request. Completable future response errors are returned as ApiError or subclasses.
   * Also see {@link #makeRequest(HttpRequestOptions)} for more information about the result completion cases.
   * @param options request options
   * @return completable future with request results
   */
  public CompletableFuture<String> request(HttpRequestOptions options) {
    return request(options, 0);
  }
  
  /**
   * Performs a request. Completable future response errors are returned as ApiError or subclasses.
   * Also see {@link #makeRequest(HttpRequestOptions)} for more information about the result completion cases.
   * @param options request options
   * @param retryCounter retry counter
   * @return completable future with request results
   */
  public CompletableFuture<String> request(HttpRequestOptions options, int retryCounter) {
    return makeCheckedRequest(options, retryCounter, Date.from(Instant.now().plusMillis(
      maxRetryDelay * retries)).getTime()).thenApply(response -> response.getBody());
  }
  
  /**
   * Does the same as {@link #request(HttpRequestOptions)} but automatically converts response into json.
   * If there is a json parsing error, completes exceptionally with {@link JsonProcessingException}.
   * @param options request options
   * @param valueType class into which the response will be transformed
   * @param <T> any DTO object that can be converted from json
   * @return completable future with request results as json
   */
  public <T> CompletableFuture<T> requestJson(HttpRequestOptions options, Class<T> valueType) {
    return requestJson(options, valueType, 0);
  }
  
  /**
   * Does the same as {@link #request(HttpRequestOptions)} but automatically converts response into json.
   * If there is a json parsing error, completes exceptionally with {@link JsonProcessingException}.
   * @param options request options
   * @param valueType class into which the response will be transformed
   * @param retryCounter retry counter
   * @param <T> any DTO object that can be converted from json
   * @return completable future with request results as json
   */
  public <T> CompletableFuture<T> requestJson(HttpRequestOptions options, Class<T> valueType, int retryCounter) {
    return request(options, retryCounter).thenApply((response) -> {
      try {
        return JsonMapper.getInstance().readValue(response, valueType);
      } catch (JsonProcessingException e) {
        throw new CompletionException(e);
      }
    });
  }

  /**
   * Performs a request. Completable future response errors are returned as ApiError or subclasses.
   * Also see {@link #makeRequest(HttpRequestOptions)} for more information about the result completion cases.
   * @param options request options
   * @param retryCounter retry counter
   * @return completable future with request response
   */
  protected CompletableFuture<HttpResponse<String>> makeCheckedRequest(
    HttpRequestOptions options, int retryCounter, long endTime) {
    return this.makeRequest(options).handle((response, error) -> {
      int localRetryCounter = retryCounter;
      int retryAfterSeconds = 0;
      if (response != null && response.getStatus() == 202) {
        retryAfterSeconds = Integer.valueOf(response.getHeaders().getFirst("retry-after"));
      }
      error = (error != null
        ? new ApiException(error.getMessage(), 0, error.getCause())
        : checkHttpError(response));
      if (error != null) {
        localRetryCounter = handleError(error, localRetryCounter, endTime).join();
        return makeCheckedRequest(options, localRetryCounter, endTime).join();
      }
      if (retryAfterSeconds != 0) {
        handleRetry(endTime, retryAfterSeconds * 1000).join();
        response = makeCheckedRequest(options, localRetryCounter, endTime).join();
      }
      return response;
    });
  }
  
  private CompletableFuture<Void> handleRetry(long endTime, int retryAfter) {
    return CompletableFuture.runAsync(() -> {
      if (endTime > Date.from(Instant.now().plusMillis(retryAfter)).getTime()) {
        try {
          Thread.sleep(retryAfter);
        } catch (InterruptedException e) {
          throw new CompletionException(e);
        }
      } else {
        throw new CompletionException(new TimeoutException(
          "Timed out waiting for the end of the process of calculating metrics"));
      }
    });
  }
  
  private CompletableFuture<Integer> handleError(Throwable error, int retryCounter, long endTime) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        if (Arrays.asList(ConflictException.class, InternalException.class, ApiException.class)
          .indexOf(error.getClass()) != - 1 && retryCounter < retries) {
          int pause = (int) Math.min(Math.pow(2, retryCounter) * minRetryDelay, maxRetryDelay);
            Thread.sleep(pause);
          return retryCounter + 1;
        } else if (error instanceof TooManyRequestsException) {
          long retryTime = ((TooManyRequestsException) error).metadata.recommendedRetryTime
            .getDate().getTime();
          if (retryTime < endTime) {
            Thread.sleep(retryTime - Date.from(Instant.now()).getTime());
            return retryCounter;
          }
        }
      } catch (InterruptedException e) {
        throw new CompletionException(e);
      }
      throw new CompletionException(error);
    });
  }
  
  /**
   * Makes request and returns HTTP response. If request fails, completable future completes exceptionally
   * with {@link UnirestException}.
   * @param options request options
   * @return completable future resolving with http response object
   */
  protected CompletableFuture<HttpResponse<String>> makeRequest(HttpRequestOptions options) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        HttpRequest<?> request = null;
        
        if (options.getMethod() == HttpRequestOptions.Method.GET) request = Unirest.get(options.getUrl());
        else {
          HttpRequestWithBody bodyRequest = null;
          switch (options.getMethod()) {
            case POST: bodyRequest = Unirest.post(options.getUrl()); break;
            case PUT: bodyRequest = Unirest.put(options.getUrl()); break;
            case DELETE: bodyRequest = Unirest.delete(options.getUrl()); break;
            default: break;
          }
          if (options.getBodyJson().isPresent()) {
            String jsonString = JsonMapper.getInstance().writeValueAsString(options.getBodyJson().get());
            request = bodyRequest.body(jsonString).header("content-type", "application/json");
          } else if (options.getBodyFields().isPresent()) {
            Map<String, Object> fields = options.getBodyFields().get();
            if (fields.isEmpty()) request = bodyRequest;
            else {
              MultipartBody multipartBody = bodyRequest.fields(null);
              fields.forEach((name, value) -> {
                if (value instanceof FileStreamField) {
                  FileStreamField fileField = (FileStreamField) value;
                  multipartBody.field(name, fileField.getStream(), fileField.getFileName());
                } else multipartBody.field(name, value.toString());
              });
              request = multipartBody;
            }
          } else request = bodyRequest;
        }
        
        HttpRequest<?> finalRequest = request;
        finalRequest.connectTimeout(connectTimeout);
        finalRequest.socketTimeout(requestTimeout);
        finalRequest.headers(options.getHeaders());
        options.getQueryParameters().forEach((key, value) -> {
          if (value instanceof Collection<?>) finalRequest.queryString(key, (Collection<?>) value);
          else finalRequest.queryString(key, value);
        });
        return finalRequest.asString();
      } catch (UnirestException | JsonProcessingException e) {
        throw new CompletionException(e);
      }
    });
  }
  
  private ApiException checkHttpError(HttpResponse<String> response) {
    try {
      int statusType = response.getStatus() / 100;
      if (statusType != 4 && statusType != 5) {
        return null;
      }
      Error error;
      try {
        error = JsonMapper.getInstance().readValue(response.getBody(), Error.class);
      } catch (JsonProcessingException e) {
        error = null;
      }
      switch (response.getStatus()) {
        case 400: return new ValidationException(
          error != null ? error.message : response.getStatusText(),
          error != null && error.details != null ? JsonMapper.getInstance().treeToValue(error.details,
            Object.class) : null
        );
        case 401: return new UnauthorizedException(error != null ? error.message : response.getStatusText());
        case 403: return new ForbiddenException(error != null ? error.message : response.getStatusText());
        case 404: return new NotFoundException(error != null ? error.message : response.getStatusText());
        case 409: return new ConflictException(error != null ? error.message : response.getStatusText());
        case 429: return new TooManyRequestsException(
          error != null ? error.message : response.getStatusText(),
          error != null && error.metadata != null ? JsonMapper.getInstance().treeToValue(error.metadata,
            TooManyRequestsExceptionMetadata.class) : null
        );
        case 500: return new InternalException(error != null ? error.message : response.getStatusText());
        default: return new ApiException(
          error != null ? error.message : response.getStatusText(),
          response.getStatus()
        );
      }
    } catch (JsonProcessingException err) {
      return new ApiException(err.getMessage(), 0, err.getCause());
    }
  }
}