package com.xyzwps.yaff.server.dto.flow;

import com.xyzwps.yaff.server.model.entity.FlowDef;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FlowSavePayload {

    @Size(max = FlowDef.MAX_DESCRIPTION_LENGTH,
            message = "description must be less than {max} characters")
    private String description;

    @NotNull(message = "data is required")
    private String data;
}
