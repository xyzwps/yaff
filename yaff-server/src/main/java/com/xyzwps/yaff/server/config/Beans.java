package com.xyzwps.yaff.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyzwps.yaff.server.commons.JSON;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {

    @Bean
    public ObjectMapper objectMapper() {
        return JSON.OM;
    }
}
