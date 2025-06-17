package com.xyzwps.libs.yaff.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public final class JSON {

    private static final ObjectMapper OM = new ObjectMapper();

    @SneakyThrows
    public static String stringify(Object value) {
        return OM.writeValueAsString(value);
    }

}
