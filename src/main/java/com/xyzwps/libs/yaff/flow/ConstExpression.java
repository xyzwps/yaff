package com.xyzwps.libs.yaff.flow;

import com.xyzwps.libs.yaff.node.Parameter;

public class ConstExpression implements AssignExpression {

    private final Object value;
    private final String inputName;

    public ConstExpression(String name, Object value) {
        this.inputName = name;
        this.value = value;
    }

    @Override
    public String getInputName() {
        return inputName;
    }

    @Override
    public Object calculate(FlowContext flowContext, Parameter.Type resultType) {
        return value;
    }
}