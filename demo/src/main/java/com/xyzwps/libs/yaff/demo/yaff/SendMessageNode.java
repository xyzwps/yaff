package com.xyzwps.libs.yaff.demo.yaff;

import com.xyzwps.libs.yaff.FlowContext;
import com.xyzwps.libs.yaff.Node;
import com.xyzwps.libs.yaff.Parameter;
import com.xyzwps.libs.yaff.ParameterType;

import java.util.List;
import java.util.Map;

public class SendMessageNode implements Node {

    public static final String NAME = "demo.sendMessage";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public List<Parameter> getInputs() {
        return List.of(
                new Parameter("title", ParameterType.STRING),
                new Parameter("message", ParameterType.STRING)
        );
    }

    @Override
    public List<Parameter> getOutputs() {
        return List.of();
    }

    @Override
    public String getDescription() {
        return "发送消息";
    }

    @Override
    public void execute(Map<String, Object> inputs, FlowContext context) {
        var title = (String) inputs.get("title");
        var message = (String) inputs.get("message");
        System.out.printf("发送消息: [%s] %s\n", title, message);
    }
}
