package com.xyzwps.yaff.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InfoService {

    @Value("${springdoc.swagger-ui.path}")
    private String swaggerUiPath;

    @Value("${server.port}")
    private int serverPort;

    public void log() {
        log.info("Swagger UI: http://localhost:{}{}", serverPort, swaggerUiPath);
    }
}
