package com.xyzwps.libs.yaff.example;

import com.xyzwps.libs.yaff.*;

class Commons {

    static final String PRINT_TEXT_NODE_NAME = "example.printText";
    static final String TEXT_TO_UPPER_NODE_NAME = "example.textToUpper";

    static final Node printTextNode = Node.builder()
            .name(PRINT_TEXT_NODE_NAME)
            .description("Prints text to console")
            .inputs(new Parameter("text", ParameterType.STRING))
            .outputs(new Parameter("cmd", ParameterType.STRING))
            .execute((inputs, context) -> {
                var textValue = inputs.get("text");
                if (textValue instanceof String text) {
                    context.set("cmd", "print(\"%s\")".formatted(text.replaceAll("\"", "\\\"")));
                } else {
                    throw new RuntimeException("Invalid input value");
                }
            })
            .build();

    static final Node textToUpperNode = Node.builder()
            .name(TEXT_TO_UPPER_NODE_NAME)
            .description("Convert text to upper case")
            .inputs(new Parameter("text", ParameterType.STRING))
            .outputs(new Parameter("text", ParameterType.STRING))
            .execute((inputs, context) -> {
                Object textValue = inputs.get("text");
                if (textValue instanceof String text) {
                    context.set("text", text.toUpperCase());
                } else {
                    throw new RuntimeException("Invalid input value");
                }
            })
            .build();

    static final FlowFactory factory = new FlowFactory()
            .register(printTextNode)
            .register(textToUpperNode);
}
