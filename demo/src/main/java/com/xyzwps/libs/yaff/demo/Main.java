
package com.xyzwps.libs.yaff.demo;


import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.xyzwps.libs.yaff.demo.commons.JSON;
import com.xyzwps.libs.yaff.demo.yaff.YaffService;
import io.helidon.http.media.jackson.JacksonMediaSupportProvider;
import io.helidon.http.media.jackson.JacksonSupport;
import io.helidon.logging.common.LogConfig;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRouting;

public class Main {

    public static void main(String[] args) {

        // load logging configuration
        LogConfig.configureRuntime();

        WebServer server = WebServer.builder()
                .config(Conf.get("server"))
                .mediaContext(b -> {
                    b.addMediaSupport(JacksonSupport.create(JSON.OM));
                })
                .routing(Main::routing)
                .build()
                .start();


        System.out.println("WEB server is up! http://localhost:" + server.port() + "/simple-greet");
    }

    static void routing(HttpRouting.Builder routing) {
        routing
                .register("/apis/greet", new GreetService())
                .register("/apis/yaff", new YaffService())
                .get("/simple-greet", (req, res) -> res.send("Hello World!"));
    }
}