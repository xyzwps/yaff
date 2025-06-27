package com.xyzwps.libs.demo.dto;

import com.xyzwps.libs.demo.entity.FlowInfo;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateFlowPayload {

    @NotNull
    private String dedupKey;

    private String description;

    @NotNull
    private String data;

    public FlowInfo toEntity() {
        return FlowInfo.builder()
            .dedupKey(dedupKey)
            .description(description)
            .data(data)
            .build();
    }
}
