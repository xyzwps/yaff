package com.xyzwps.yaff.core;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.xyzwps.yaff.core.commons.Utils;

import static com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type")
@JsonSubTypes({
        @Type(value = JavaScriptExpression.class, name = JavaScriptExpression.TYPE)
})
public sealed interface AssignExpression permits JavaScriptExpression {

    String getInputName();

    String getType();

    Object calculate(FlowContext flowContext, Class<?> resultType);


    static String validInputName(String inputName) {
        if (inputName == null || inputName.isEmpty()) {
            throw new YaffException("inputName cannot be null or empty");
        }
        if (!Utils.isIdentifier(inputName)) {
            throw new YaffException("Invalid inputName: " + inputName);
        }
        return inputName;
    }
}
