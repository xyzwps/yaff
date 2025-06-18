package com.xyzwps.libs.yaff;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type")
@JsonSubTypes({
        @Type(value = ConstantExpression.class, name = ConstantExpression.TYPE),
        @Type(value = JavaScriptExpression.class, name = JavaScriptExpression.TYPE)
})
public sealed interface AssignExpression permits ConstantExpression, JavaScriptExpression {

    String getInputName();

    String getType();

    Object calculate(FlowContext flowContext, ParameterType resultType);
}
