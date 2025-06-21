package com.xyzwps.libs.yaff.demo.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowRow {
    private Integer id;
    private String dedupKey;
    private String description;
    private String data;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
