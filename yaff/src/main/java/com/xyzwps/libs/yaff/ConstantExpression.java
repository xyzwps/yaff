package com.xyzwps.libs.yaff;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class ConstantExpression implements AssignExpression {

    private Object value;
    private String inputName;

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
    public Object calculate(FlowContext flowContext, ParameterType resultType) {
        return value;
    }
}