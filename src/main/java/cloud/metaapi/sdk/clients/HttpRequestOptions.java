package cloud.metaapi.sdk.clients;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Options for HttpClient requests
 */
public class HttpRequestOptions {
    
    /**
     * File field wrapper for use in form data body. It is better to use this class for file fields
     * because it explicitly gives opportunity to set files as {@link File} or {@link InputStream}.
     */
    public static class FileStreamField {
        
        private InputStream stream;
        private String fileName;
        
        /**
         * Constructs the file field wrapper from {@link File} object
         * @param file file object which will be sent in request
         * @throws FileNotFoundException if file not found
         */
        public FileStreamField(File file) throws FileNotFoundException {
            setFile(file);
        }
        
        /**
         * Constructs the file field wrapper from {@link InputStream} and the file name
         * @param stream input stream with contents of the file
         * @param fileName file name under which the file will be sent
         */
        public FileStreamField(InputStream stream, String fileName) {
            setFile(stream, fileName);
        }
        
        /**
         * Sets file from {@link File} object
         * @param file file object which will be sent in request
         * @throws FileNotFoundException if file not found
         */
        public void setFile(File file) throws FileNotFoundException {
            this.setFile(new FileInputStream(file), file.getName());
        }
        
        /**
         * Sets file from {@link InputStream} object
         * @param stream input stream with contents of the file
         * @param fileName file name under which the file will be sent
         */
        public void setFile(InputStream stream, String fileName) {
            this.stream = stream;
            this.fileName = fileName;
        }
        
        /**
         * Returns {@link InputStream} with contents of the file
         * @return input stream
         */
        public InputStream getStream() {
            return stream;
        }
        
        /**
         * Returns the file name under which the file will be sent in request
         * @return file name
         */
        public String getFileName() {
            return fileName;
        }
    }

    /**
     * Represents HTTP request methods supported by HttpClient
     */
    public enum Method { GET, POST, PUT, DELETE }
    
    private String url;
    private HttpRequestOptions.Method method;
    private Map<String, Object> queryParameters = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private Optional<Map<String, Object>> bodyFields = Optional.empty();
    private Optional<Object> bodyJson = Optional.empty();
    
    /**
     * Constructs HttpRequestOptions instance
     * @param url request URL
     * @param method request method
     */
    public HttpRequestOptions(String url, Method method) {
        setUrl(url);
        setMethod(method);
    }
    
    /**
     * Sets URL used for request
     * @param url new request URL
     */
    public void setUrl(String url) {
        this.url = url;
    }
    
    /**
     * Returns URL used for request
     * @return request URL
     */
    public String getUrl() {
        return url;
    }
    
    /**
     * Sets HTTP method used for request
     * @param method supported by HttpClient HTTP method
     */
    public void setMethod(Method method) {
        this.method = method;
    }
    
    /**
     * Returns HTTP method used for request
     * @return request method
     */
    public Method getMethod() {
        return method;
    }
    
    /**
     * Returns query parameters of request. The returned value can be used for setting parameters.
     * @return map of query parameters where the key is the parameter name and the value is its 
     * parsable into a string value. The value can also be a {@link Collection} of parsable into
     * a string values.
     */
    public Map<String, Object> getQueryParameters() {
        return queryParameters;
    }
    
    /**
     * Returns headers of request. The returned value can be used for setting parameters.
     * @return map of headers where the key is the header name and the value is the header value
     */
    public Map<String, String> getHeaders() {
        return headers;
    }
    
    /**
     * Sets the json body of a request. If there already is a form data body, it will be erased.
     * @param jsonBody object that will be automatically converted into a json string
     */
    public void setBody(Object jsonBody) {
        this.bodyJson = Optional.of(jsonBody);
        this.bodyFields = Optional.empty();
    }
    
    /**
     * Sets the form data body of a request. If there already is a json body, it will erased.
     * @param fields map of form fields where the key is the name of a field and the value should be 
     * an object that can be parsed as a string, but for files you should use {@link FileStreamField}
     */
    public void setBody(Map<String, Object> fields) {
        this.bodyFields = Optional.of(fields);
        this.bodyJson = Optional.empty();
    }
    
    /**
     * Returns the json body of a request
     * @return optional object that can contain json body of a request in its object form
     */
    public Optional<Object> getBodyJson() {
        return bodyJson;
    }
    
    /**
     * Returns fields of the request form data body
     * @return optional object that can contain a map of form data fields where the key is 
     * the name of the field and the value is its value which was set earlier
     */
    public Optional<Map<String, Object>> getBodyFields() {
        return bodyFields;
    }
}