package com.xyzwps.libs.yaff;

public interface AssignExpression {

    String getInputName();

    String getType();

    Object calculate(FlowContext flowContext, ParameterType resultType);
}
