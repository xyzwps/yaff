package com.xyzwps.libs.yaff.demo.yaff;

import com.xyzwps.libs.yaff.FlowContext;
import com.xyzwps.libs.yaff.Node;
import com.xyzwps.libs.yaff.Parameter;
import com.xyzwps.libs.yaff.ParameterType;

import java.util.List;
import java.util.Map;

public class RNGNode implements Node {

    public static final String NAME = "demo.rng";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public List<Parameter> getInputs() {
        return List.of(
                new Parameter("min", ParameterType.FLOAT),
                new Parameter("max", ParameterType.FLOAT)
        );
    }

    @Override
    public List<Parameter> getOutputs() {
        return List.of(
                new Parameter("result", ParameterType.FLOAT)
        );
    }

    @Override
    public String getDescription() {
        return "生成一个随机数";
    }

    @Override
    public void execute(Map<String, Object> inputs, FlowContext context) {
        var min = (Float) inputs.get("min");
        var max = (Float) inputs.get("max");
        var result = (float) (Math.random() * (max - min) + min);
        context.set("result", result);
    }
}
