package com.xyzwps.libs.demo.dto;

import com.xyzwps.libs.demo.entity.FlowInfo;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateFlowPayload {

    @NotNull
    private Integer id;

    private String description;

    @NotNull
    private String data;

    public FlowInfo toEntity() {
        return FlowInfo.builder()
                .id(id)
                .description(description)
                .data(data)
                .build();
    }
}
