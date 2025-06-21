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
import java.util.Objects;

public class YaffService implements HttpService {

    @Override
    public void routing(HttpRules httpRules) {
        httpRules.get("/metadata", this::getMetaData);
        httpRules.get("/flows", this::getAllFlows);
        httpRules.post("/flows", this::createFlow);
        httpRules.get("/flows/{id}", this::getFlow);
        httpRules.put("/flows/{id}", this::updateFlow);
        httpRules.delete("/flows/{id}", this::deleteFlow);

    }

    private void getMetaData(ServerRequest request, ServerResponse response) {
        response.send(Yaff.getMetaData());
    }

    private void getAllFlows(ServerRequest request, ServerResponse response) {
        response.send(DB.getAllFlows());
    }

    private void getFlow(ServerRequest request, ServerResponse response) {
        var idStr = request.path().pathParameters().get("id");
        var id = Integer.parseInt(idStr);
        var row = DB.getFlow(id);
        if (row == null) {
            response.status(404).send();
        } else {
            response.send(row);
        }
    }

    private void deleteFlow(ServerRequest request, ServerResponse response) {
        var idStr = request.path().pathParameters().get("id");
        var id = Integer.parseInt(idStr);
        var row = DB.getFlow(id);
        if (row == null) {
            response.status(404).send();
        } else {
            DB.deleteFlow(id);
            response.status(204).send();
        }
    }

    // TODO: 更细致的检查
    private void updateFlow(ServerRequest request, ServerResponse response) {
        var idStr = request.path().pathParameters().get("id");
        var id = Integer.parseInt(idStr);
        var oldRow = DB.getFlow(id);
        if (oldRow == null) {
            response.status(404).send();
        } else {
            var row = request.content().as(FlowRow.class);
            if (!Objects.equals(row.getId(), id)) {
                response.status(400).send();
            } else {
                Yaff.FACTORY.fromJSON(row.getData());
                DB.updateFlow(row);
                response.status(204);
            }
        }
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
