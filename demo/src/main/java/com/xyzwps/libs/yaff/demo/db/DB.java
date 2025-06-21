package com.xyzwps.libs.yaff.demo.db;

import com.xyzwps.libs.yaff.demo.Conf;
import io.helidon.dbclient.DbClient;
import io.helidon.dbclient.DbRow;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DB {

    public static final DbClient client = DbClient.create(Conf.get("db"));

    static {
        client.execute().dml(DDL.TABLE_FLOWS);
    }


    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public static List<FlowRow> getAllFlows() {
        var sql = """
                SELECT id, dedup_key, description, data, created_at, updated_at
                FROM flows
                """;
        return client.execute()
                .createQuery(sql)
                .execute()
                .map(DB::dbRowToFlowRow)
                .toList();
    }

    private static FlowRow dbRowToFlowRow(DbRow it) {
        return FlowRow.builder()
                .id(it.column("id".toUpperCase()).get(Integer.class))
                .dedupKey(it.column("dedup_key".toUpperCase()).get(String.class))
                .description(it.column("description".toUpperCase()).get(String.class))
                .data(it.column("data".toUpperCase()).get(String.class))
                .createdAt(it.column("created_at".toUpperCase()).get(LocalDateTime.class))
                .updatedAt(it.column("updated_at".toUpperCase()).get(LocalDateTime.class))
                .build();
    }

    public static FlowRow getFlow(int id) {
        var sql = """
                SELECT id, dedup_key, description, data, created_at, updated_at
                FROM flows WHERE id = :id
                """;
        return client.execute()
                .createQuery(sql)
                .addParam("id", id)
                .execute()
                .map(DB::dbRowToFlowRow)
                .findFirst().orElse(null);
    }


    public static void deleteFlow(int id) {
        var sql = "DELETE FROM flows WHERE id = ?";
        client.execute().delete(sql, id);
    }

    public static void updateFlow(FlowRow row) {
        var sql = """
                UPDATE flows
                SET description = ?, data = ?, updated_at = ?
                WHERE id = ?
                """;
        client.execute()
                .update(sql, row.getDescription(), row.getData(), row.getUpdatedAt(), row.getId());
    }

    public static void insertFlow(FlowRow row) {
        row.setCreatedAt(LocalDateTime.now());
        row.setUpdatedAt(LocalDateTime.now());
        var sql = """
                insert into flows ( dedup_key, description, data, created_at, updated_at )
                values (:dedupKey, :description, :data, :createdAt, :updatedAt)
                """;
        client.execute()
                .createInsert(sql)
                .addParam("dedupKey", row.getDedupKey())
                .addParam("description", row.getDescription())
                .addParam("data", row.getData())
                .addParam("createdAt", row.getCreatedAt().format(DTF))
                .addParam("updatedAt", row.getUpdatedAt().format(DTF))
                .execute();
    }
}
