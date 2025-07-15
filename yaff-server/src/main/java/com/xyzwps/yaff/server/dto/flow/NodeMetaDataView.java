package com.xyzwps.yaff.server.dto.flow;

import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.xyzwps.yaff.core.NodeMetaData;
import com.xyzwps.yaff.core.commons.JSON;

import java.util.List;

public record NodeMetaDataView(String name,
                               String description,
                               List<InputView> inputs,
                               OutputView output) {

    public record InputView(String name, JsonSchema schema) {
    }

    public record OutputView(JsonSchema schema) {
    }

    public static NodeMetaDataView from(NodeMetaData nodeMetaData) {
        var name = nodeMetaData.name();
        var description = nodeMetaData.description();
        var inputs = nodeMetaData.inputs();
        var output = nodeMetaData.output();

        return new NodeMetaDataView(name, description,
                inputs.stream().map(input -> new InputView(input.name(), JSON.toJsonSchema(input.type()))).toList(),
                output == null ? null : new OutputView(JSON.toJsonSchema(output.type())));
    }
}
