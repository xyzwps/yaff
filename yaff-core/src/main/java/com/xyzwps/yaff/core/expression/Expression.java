package com.xyzwps.yaff.core.expression;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.xyzwps.yaff.core.FlowContext;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = JavaScriptExpression.class, name = JavaScriptExpression.TYPE)
})
public sealed interface Expression permits JavaScriptExpression {
    String getType();

    <T> T calculate(FlowContext flowContext, Class<T> resultType);
}
