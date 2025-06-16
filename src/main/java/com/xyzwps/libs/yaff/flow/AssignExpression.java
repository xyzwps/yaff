package com.xyzwps.libs.yaff.flow;

import com.xyzwps.libs.yaff.node.Parameter;
import com.xyzwps.libs.yaff.node.ParameterType;

public interface AssignExpression {

    String getInputName();

    Object calculate(FlowContext flowContext, ParameterType resultType);
}
