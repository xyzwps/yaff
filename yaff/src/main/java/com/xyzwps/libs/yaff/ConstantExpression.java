package com.xyzwps.libs.yaff;

public class ConstantExpression implements AssignExpression {

    private final Object value;
    private final String inputName;

    public static final String TYPE = "constant";

    @Override
    public String getType() {
        return TYPE;
    }

    public ConstantExpression(String name, Object value) {
        this.inputName = name;
        this.value = ParameterType.valid(value);
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