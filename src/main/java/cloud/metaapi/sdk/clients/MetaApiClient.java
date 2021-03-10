package cloud.metaapi.sdk.clients;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * metaapi.cloud MetaTrader API client
 */
public class MetaApiClient {
    
    protected HttpClient httpClient;
    protected String host;
    protected String token;
    
    /**
     * Constructs MetaTrader API client instance. Domain is set to {@code agiliumtrade.agiliumtrade.ai} 
     * @param httpClient HTTP client
     * @param token authorization token
     */
    public MetaApiClient(HttpClient httpClient, String token) {
        this(httpClient, token, "agiliumtrade.agiliumtrade.ai");
    }
    
    /**
     * Constructs MetaTrader API client instance
     * @param httpClient HTTP client
     * @param token authorization token
     * @param domain domain to connect to
     */
    public MetaApiClient(HttpClient httpClient, String token, String domain) {
        this.httpClient = httpClient;
        this.host = "https://mt-provisioning-api-v1." + domain;
        this.token = token;
    }
    
    /**
     * Returns type of current token
     * @return Type of current token
     */
    protected String getTokenType() {
        if (token.split("\\.").length == 3) return "api";
        if (token.split("\\.").length == 1) return "account";
        return "";
    }
    
    /**
     * Checks that current token is not api token
     * @return Indicator of absence api token
     */
    protected boolean isNotJwtToken() {
        return token.split("\\.").length != 3;
    }
    
    /**
     * Checks that current token is not account token
     * @return Indicator of absence account token
     */
    protected boolean isNotAccountToken() {
        return token.split("\\.").length != 1;
    }
    
    /**
     * Handles no accessing to the method
     * @param methodName Name of method
     * @param <T> any type returned by the calling method
     * @return completable future with completed with method access exception
     */
    protected <T> CompletableFuture<T> handleNoAccessError(String methodName) {
    	return CompletableFuture.supplyAsync(() -> {
    		throw new CompletionException(new MethodAccessException(methodName, getTokenType()));
    	});
    }
}