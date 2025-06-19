package com.xyzwps.libs.yaff.example;

import com.xyzwps.libs.yaff.*;

import java.util.List;

class Commons {

    static final String PRINT_TEXT_NODE_NAME = "example.printText";
    static final String TEXT_TO_UPPER_NODE_NAME = "example.textToUpper";

    static final Node printTextNode = new NodeTemplate(
            PRINT_TEXT_NODE_NAME,
            "Prints text to console",
            List.of(new Parameter("text", ParameterType.STRING)),
            List.of(new Parameter("cmd", ParameterType.STRING)),
            (inputs, context) -> {
                var textValue = inputs.get("text");
                if (textValue instanceof String text) {
                    context.set("cmd", "print(\"%s\")".formatted(text.replaceAll("\"", "\\\"")));
                } else {
                    throw new RuntimeException("Invalid input value");
                }
            }
    );

    static final Node textToUpperNode = new NodeTemplate(
            TEXT_TO_UPPER_NODE_NAME,
            "Convert text to upper case",
            List.of(new Parameter("text", ParameterType.STRING)),
            List.of(new Parameter("text", ParameterType.STRING)),
            (inputs, context) -> {
                var textValue = inputs.get("text");
                if (textValue instanceof String text) {
                    context.set("text", text.toUpperCase());
                } else {
                    throw new RuntimeException("Invalid input value");
                }
            }
    );

    static final FlowFactory factory = new FlowFactory()
            .register(printTextNode)
            .register(textToUpperNode);
}
