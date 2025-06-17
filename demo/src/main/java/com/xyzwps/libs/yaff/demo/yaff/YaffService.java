package com.xyzwps.libs.yaff.demo.yaff;

import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;

public class YaffService implements HttpService {

    @Override
    public void routing(HttpRules httpRules) {
        httpRules.get("/metadata", this::getMetaData);
    }

    private void getMetaData(ServerRequest request, ServerResponse response) {
        response.send(Yaff.getMetaData());
    }

}
