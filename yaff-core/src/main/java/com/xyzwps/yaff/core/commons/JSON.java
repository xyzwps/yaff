package com.xyzwps.yaff.core.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public final class JSON {

    private static final ObjectMapper OM = new ObjectMapper();

    @SneakyThrows
    public static String stringify(Object value) {
        return OM.writeValueAsString(value);
    }

    @SneakyThrows
    public static <T> T parse(String json, Class<T> clazz) {
        return OM.readValue(json, clazz);
    }

}
