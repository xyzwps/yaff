package com.xyzwps.yaff.core.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import lombok.NonNull;
import lombok.SneakyThrows;

public final class JSON {

    private static final ObjectMapper OM = new ObjectMapper();

    @SneakyThrows
    public static String stringify(Object value) {
        return OM.writeValueAsString(value);
    }

    @SneakyThrows
    public static String pretty(Object value) {
        return OM.writerWithDefaultPrettyPrinter().writeValueAsString(value);
    }

    @SneakyThrows
    public static <T> T parse(String json, @NonNull Class<T> clazz) {
        return OM.readValue(json, clazz);
    }

    @SneakyThrows
    public static JsonSchema toJsonSchema(@NonNull Class<?> clazz) {
        // TODO: cache
        var visitor = new SchemaFactoryWrapper();
        OM.acceptJsonFormatVisitor(clazz, visitor);
        return visitor.finalSchema();
    }

}
