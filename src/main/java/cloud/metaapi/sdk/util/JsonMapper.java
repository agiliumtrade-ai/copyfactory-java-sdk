package cloud.metaapi.sdk.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class for getting a singleton instance of a configured JSON object mapper
 */
public class JsonMapper {
    
    private static ObjectMapper mapper = null;
    
    /**
     * Returns a singleton instance of a configured JSON object mapper
     * @return json object mapper instance
     */
    public static ObjectMapper getInstance() {
        if (mapper == null) {
            mapper = new ObjectMapper();
            mapper.setSerializationInclusion(Include.NON_NULL);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return mapper;
    }
    
    private JsonMapper() {}
}