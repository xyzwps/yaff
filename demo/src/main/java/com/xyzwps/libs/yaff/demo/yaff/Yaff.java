package com.xyzwps.libs.yaff.demo.yaff;

import com.xyzwps.libs.yaff.*;

import java.util.List;

public class Yaff {

    public static final String SEND_MSG_NODE_NAME = "demo.sendMessage";


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
    }

    public static List<NodeMetaData> getMetaData() {
        return FACTORY.getNodeRegister().getMetaData();
    }

}
