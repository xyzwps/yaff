package com.xyzwps.yaff.server;

import com.xyzwps.yaff.server.service.InfoService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        var ctx = SpringApplication.run(ServerApplication.class, args);
        ctx.getBean(InfoService.class).log();
    }

}
