package com.xyzwps.libs.yaff.demo.db;

final class DDL {

    static final String TABLE_FLOWS = """
            CREATE TABLE IF NOT EXISTS flows (
                id          INT         AUTO_INCREMENT PRIMARY KEY,
                dedup_key   VARCHAR(60) UNIQUE NOT NULL,
                description VARCHAR(60),
                data        VARCHAR(100000)    NOT NULL,
                created_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
                updated_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
            );
            """;
}
