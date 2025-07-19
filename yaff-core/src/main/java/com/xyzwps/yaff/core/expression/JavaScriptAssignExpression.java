package com.xyzwps.yaff.core.expression;

import com.xyzwps.yaff.core.FlowContext;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class JavaScriptAssignExpression implements AssignExpression {
    private String expression; // TODO: 想个办法验证表达式
    private String inputName;

    public static final String TYPE = "javascript";

    public JavaScriptAssignExpression(String name, String expression) {
        this.inputName = AssignExpression.validInputName(name);
        this.expression = expression;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public void setInputName(String inputName) {
        this.inputName = AssignExpression.validInputName(inputName);
    }

    @Override
    public Object calculate(FlowContext flowContext, Class<?> resultType) {
        return JavaScriptExpression.calculate(expression, flowContext, resultType);
    }
}
