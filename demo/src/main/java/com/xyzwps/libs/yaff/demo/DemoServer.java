
package com.xyzwps.libs.yaff.demo;


import com.xyzwps.libs.yaff.demo.yaff.YaffService;
import io.helidon.logging.common.LogConfig;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRouting;

public class DemoServer {

    public static void main(String[] args) {

        // load logging configuration
        LogConfig.configureRuntime();

        WebServer server = WebServer.builder()
                .config(Conf.get("server"))
                .routing(DemoServer::routing)
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