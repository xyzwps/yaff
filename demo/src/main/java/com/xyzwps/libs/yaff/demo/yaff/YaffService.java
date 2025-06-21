package com.xyzwps.libs.yaff.demo.yaff;

import com.xyzwps.libs.yaff.demo.db.DB;
import com.xyzwps.libs.yaff.demo.db.FlowRow;
import io.helidon.http.Status;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;

import java.util.Map;

public class YaffService implements HttpService {

    @Override
    public void routing(HttpRules httpRules) {
        httpRules.get("/metadata", this::getMetaData);
        httpRules.get("/flows", this::getAllFlows);
        httpRules.post("/flows", this::createFlow);
    }

    private void getMetaData(ServerRequest request, ServerResponse response) {
        response.send(Yaff.getMetaData());
    }

    private void getAllFlows(ServerRequest request, ServerResponse response) {
        response.send(DB.getAllFlows());
    }

    private void createFlow(ServerRequest request, ServerResponse response) {
        var row = request.content().as(FlowRow.class);
        try {
            Yaff.FACTORY.fromJSON(row.getData());
            DB.insertFlow(row);
        } catch (Exception e) {
            for (Throwable cause = e; cause != null; cause = cause.getCause()) {
                if (cause instanceof JdbcSQLIntegrityConstraintViolationException cve) {
                    response.status(Status.CONFLICT_409);
                    response.send(Map.of("message", e.getMessage()));
                    return;
                }
            }
            response.status(Status.INTERNAL_SERVER_ERROR_500);
            response.send(Map.of("message", e.getMessage()));
            return;
        }
        response.send(Map.of("ok", true));
    }


}
