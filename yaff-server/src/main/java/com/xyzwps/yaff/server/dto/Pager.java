package com.xyzwps.yaff.server.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class Pager {
    @Parameter(description = "1-based page number", in = ParameterIn.QUERY, example = "1",
            schema = @Schema(defaultValue = "1"))
    @Min(value = 1, message = "page must be ge {value}")
    public int page = 1;

    @Parameter(description = "Page size", in = ParameterIn.QUERY, example = "10",
            schema = @Schema(defaultValue = "10"))
    @Min(value = 1, message = "size must be ge {value}")
    @Max(value = 100, message = "size must be le {value}")
    public int size = 10;
}
