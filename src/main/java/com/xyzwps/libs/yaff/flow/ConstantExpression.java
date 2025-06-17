package com.xyzwps.libs.yaff.flow;

import com.xyzwps.libs.yaff.node.ParameterType;

public class ConstantExpression implements AssignExpression {

    private final Object value;
    private final String inputName;

    public static final String TYPE = "constant";

    @Override
    public String getType() {
        return TYPE;
    }

    public ConstantExpression(String name, Object value) {
        // TODO: 检查类型
        this.inputName = name;
        this.value = value;
    }

    @Override
    public String getInputName() {
        return inputName;
    }

    @Override
    public Object calculate(FlowContext flowContext, ParameterType resultType) {
        return value;
    }
}