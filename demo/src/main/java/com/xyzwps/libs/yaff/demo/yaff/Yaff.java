package com.xyzwps.libs.yaff.demo.yaff;

import com.xyzwps.libs.yaff.*;

import java.util.List;

public class Yaff {

    public static final String RNG_NODE_NAME = "demo.rng";
    public static final String SEND_MSG_NODE_NAME = "demo.sendMessage";

    // TODO: 测试随机数
    static Node RNG_NODE = Node.builder()
            .name(RNG_NODE_NAME)
            .description("生成一个随机数")
            .inputs(new NodeInput("min", ParameterType.FLOAT),
                    new NodeInput("max", ParameterType.FLOAT))
            .output(new NodeOutput(ParameterType.FLOAT))
            .execute((inputs) -> {
                var min = (Float) inputs.get("min");
                var max = (Float) inputs.get("max");
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
                System.out.printf("发送消息: [%s] %s\n", title, message);
                return null;
            })
            .build();

    public static final FlowFactory FACTORY = new FlowFactory();

    static {
        FACTORY.register(SEND_MSG_NODE);
        FACTORY.register(RNG_NODE);
    }

    public static List<NodeMetaData> getMetaData() {
        return FACTORY.getNodeRegister().getMetaData();
    }

}
