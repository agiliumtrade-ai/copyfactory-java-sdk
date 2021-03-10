package cloud.metaapi.sdk.clients.models;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Wrapper for setting and getting model time fields as {@link Date} objects and in ISO format strings.
 * Such fields are automatically serialized into and deserialized from JSON. In JSON they are
 * represented as ISO strings.
 */
@JsonSerialize(using = IsoTimeJsonSerializer.class)
@JsonDeserialize(using = IsoTimeJsonDeserializer.class)
public class IsoTime {
    
    private Date date;
    
    /**
     * Constructs wrapper from current time
     */
    public IsoTime() {
        this(Date.from(Instant.now()));
    }
    
    /**
     * Constructs wrapper from ISO time string
     * @param isoTime ISO time
     */
    public IsoTime(String isoTime) {
        setTime(isoTime);
    }
    
    /**
     * Constructs wrapper from {@link Date} object
     * @param date date object
     */
    public IsoTime(Date date) {
        setTime(date);
    }
    
    /**
     * Sets wrapper time from ISO format time
     * @param isoTime ISO format time
     */
    public void setTime(String isoTime) {
        date = Date.from(ZonedDateTime.parse((String) isoTime).toInstant());
    }
    
    /**
     * Sets wrapper time from {@link Date} object
     * @param date date object
     */
    public void setTime(Date date) {
        this.date = date;
    }
    
    /**
     * Returns wrapper time in ISO format
     * @return ISO format time
     */
    public String getIsoString() {
        return ZonedDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
    }
    
    /**
     * Returns wrapper time as {@link Date} object
     * @return date object
     */
    public Date getDate() {
        return date;
    }
    
    /**
     * Represents the wrapper as ISO format time string
     */
    @Override
    public String toString() {
        return this.getIsoString();
    }
}

/**
 * JSON serializer for converting the ISO time wrapper into a JSON string
 */
class IsoTimeJsonSerializer extends StdSerializer<IsoTime> {
    
    private static final long serialVersionUID = 1L;

    /**
     * Constructs serializer
     */
    public IsoTimeJsonSerializer() {
        this(null);
    }
    
    /**
     * Constructs serializer from the wrapper class
     * @param time wrapper class
     */
    public IsoTimeJsonSerializer(Class<IsoTime> time) {
        super(time);
    }
    
    @Override
    public void serialize(IsoTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(value.getIsoString());
    }
}

/**
 * JSON deserializer for converting a JSON string into the ISO time wrapper
 */
class IsoTimeJsonDeserializer extends StdDeserializer<IsoTime> {
    
    private static final long serialVersionUID = 1L;

    /**
     * Constructs deserializer
     */
    public IsoTimeJsonDeserializer() {
        this(null);
    }
    
    /**
     * Constructs deserializer from the wrapper class
     * @param time wrapper class
     */
    public IsoTimeJsonDeserializer(Class<IsoTime> t) {
        super(t);
    }
    
    @Override
    public IsoTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String time = jp.getCodec().readValue(jp, String.class);
        return new IsoTime(time);
    }
}