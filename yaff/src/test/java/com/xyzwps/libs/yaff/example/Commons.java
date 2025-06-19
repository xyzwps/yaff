package com.xyzwps.libs.yaff.example;

import com.xyzwps.libs.yaff.*;

import java.util.ArrayList;

class Commons {

    static final String PRINT_TEXT_NODE_NAME = "example.printText";
    static final String TEXT_TO_UPPER_NODE_NAME = "example.textToUpper";

    public static final String RNG_NODE_NAME = "demo.rng";
    public static final String SEND_MSG_NODE_NAME = "demo.sendMessage";

    public static final ArrayList<String> MESSAGES_RECEIVER = new ArrayList<>();

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

    static Node RNG_NODE = Node.builder()
            .name(RNG_NODE_NAME)
            .description("生成一个随机数")
            .inputs(new NodeInput("min", ParameterType.FLOAT),
                    new NodeInput("max", ParameterType.FLOAT))
            .output(new NodeOutput(ParameterType.FLOAT))
            .execute((inputs) -> {
                var min = ((Number) inputs.get("min")).doubleValue();
                var max = ((Number) inputs.get("max")).doubleValue();
                return (float) (Math.random() * (max - min) + min);
            }).build();

    static Node SEND_MSG_NODE = Node.builder()
            .name(SEND_MSG_NODE_NAME)
            .description("发送消息")
            .inputs(new NodeInput("title", ParameterType.STRING),
                    new NodeInput("message", ParameterType.STRING))
            .execute((inputs) -> {
                var title = (String) inputs.get("title");
                var message = (String) inputs.get("message");
                MESSAGES_RECEIVER.add("[%s]: %s".formatted(title, message));
                return null;
            })
            .build();

    static final FlowFactory factory = new FlowFactory()
            .register(printTextNode)
            .register(textToUpperNode)
            .register(RNG_NODE)
            .register(SEND_MSG_NODE);
}
