package com.xyzwps.libs.yaff.example;

import com.xyzwps.libs.yaff.FlowContext;
import com.xyzwps.libs.yaff.Node;
import com.xyzwps.libs.yaff.Parameter;
import com.xyzwps.libs.yaff.ParameterType;

import java.util.List;
import java.util.Map;

class TextToUpperNode implements Node {

    public static final String NAME = "example.textToUpper";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public List<Parameter> getInputs() {
        return List.of(new Parameter("text", ParameterType.STRING));
    }

    @Override
    public List<Parameter> getOutputs() {
        return List.of(new Parameter("text", ParameterType.STRING));
    }

    @Override
    public String getDescription() {
        return "把输入文本转为大写形式";
    }

    @Override
    public void execute(Map<String, Object> inputs, FlowContext context) {
        var textValue = inputs.get("text");
        if (textValue instanceof String text) {
            context.set("text", text.toUpperCase());
        } else {
            throw new RuntimeException("Invalid input value");
        }
    }
}
