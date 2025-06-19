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
            .inputs(new Parameter("min", ParameterType.FLOAT),
                    new Parameter("max", ParameterType.FLOAT))
            .outputs(new Parameter("result", ParameterType.FLOAT))
            .execute((inputs, context) -> {
                var min = (Float) inputs.get("min");
                var max = (Float) inputs.get("max");
                var result = (float) (Math.random() * (max - min) + min);
                context.set("result", result);
            }).build();

    static Node SEND_MSG_NODE = Node.builder()
            .name(SEND_MSG_NODE_NAME)
            .description("发送消息")
            .inputs(new Parameter("title", ParameterType.STRING),
                    new Parameter("message", ParameterType.STRING))
            .execute((inputs, context) -> {
                var title = (String) inputs.get("title");
                var message = (String) inputs.get("message");
                System.out.printf("发送消息: [%s] %s\n", title, message);
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
