package com.xyzwps.libs.yaff.flow;

import com.xyzwps.libs.yaff.node.Parameter;

public interface AssignExpression {

    String getInputName();

    Object calculate(FlowContext flowContext, Parameter.Type resultType);
}
