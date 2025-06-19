package com.xyzwps.libs.yaff.example;

import com.xyzwps.libs.yaff.*;

class Commons {

    static final String PRINT_TEXT_NODE_NAME = "example.printText";
    static final String TEXT_TO_UPPER_NODE_NAME = "example.textToUpper";

    static final Node printTextNode = Node.builder()
            .name(PRINT_TEXT_NODE_NAME)
            .description("Prints text to console")
            .inputs(new NodeInput("text", ParameterType.STRING))
            .output(new NodeOutput(ParameterType.STRING))
            .execute((inputs) -> {
                var textValue = inputs.get("text");
                if (textValue instanceof String text) {
                    return "print(\"%s\")".formatted(text.replaceAll("\"", "\\\""));
                }
                throw new RuntimeException("Invalid inputs value");
            })
            .build();

    static final Node textToUpperNode = Node.builder()
            .name(TEXT_TO_UPPER_NODE_NAME)
            .description("Convert text to upper case")
            .inputs(new NodeInput("text", ParameterType.STRING))
            .output(new NodeOutput(ParameterType.STRING))
            .execute((inputs) -> {
                Object textValue = inputs.get("text");
                if (textValue instanceof String text) {
                    return text.toUpperCase();
                }
                throw new RuntimeException("Invalid inputs value");
            })
            .build();

    static final FlowFactory factory = new FlowFactory()
            .register(printTextNode)
            .register(textToUpperNode);
}
