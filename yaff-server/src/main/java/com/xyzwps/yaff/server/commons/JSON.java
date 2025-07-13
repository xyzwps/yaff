package com.xyzwps.yaff.server.commons;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JSON {
    public static final ObjectMapper OM = createMapper();

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static ObjectMapper createMapper() {

        var j8Time = new JavaTimeModule();
        j8Time.addSerializer(LocalDateTime.class, new JsonSerializer4LocalDateTime());
        j8Time.addDeserializer(LocalDateTime.class, new JsonDeserializer4LocalDateTime());

        return JsonMapper.builder()
                .enable(StreamReadFeature.USE_FAST_DOUBLE_PARSER)
                .enable(StreamReadFeature.USE_FAST_BIG_NUMBER_PARSER)
                .build()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(j8Time);
    }

    static class JsonDeserializer4LocalDateTime extends StdDeserializer<LocalDateTime> {

        JsonDeserializer4LocalDateTime() {
            super(LocalDateTime.class);
        }

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            if (p.hasToken(JsonToken.VALUE_NULL)) {
                return null;
            }

            if (p.hasToken(JsonToken.VALUE_STRING)) {
                return LocalDateTime.parse(p.getText(), DTF);
            }

            throw new JsonParseException(p, "Expected string or null");
        }
    }


    static class JsonSerializer4LocalDateTime extends StdSerializer<LocalDateTime> {

        JsonSerializer4LocalDateTime() {
            super(LocalDateTime.class);
        }

        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if (value == null) {
                gen.writeNull();
            } else {
                gen.writeString(value.format(DTF));
            }
        }
    }
}
