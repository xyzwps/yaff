package com.xyzwps.yaff.core;

import com.xyzwps.yaff.core.commons.JSON;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

@Data
@NoArgsConstructor
public final class JavaScriptExpression implements AssignExpression {
    private String expression; // TODO: 想个办法验证表达式
    private String inputName;

    public static final String TYPE = "javascript";

    public JavaScriptExpression(String name, String expression) {
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
        try (var context = Context.create()) {
            var script = makeScript(flowContext, expression);
            var value = context.eval("js", script);
            return convert(value, resultType);
        }
    }

    private static Object convert(Value value, Class<?> resultType) {
        if (value == null) {
            return null;
        }
        if (value.isNull()) {
            return null;
        }
        return JSON.parse(value.asString(), resultType);
    }


    /**
     * 把 flowContext 转换成 JavaScript 脚本。
     *
     * @param flowContext 上下文
     * @return 脚本
     * @see SimpleFlowContext#set
     */
    private static String makeScript(FlowContext flowContext, String expression) {
        var scripts = new StringBuilder();
        for (var name : flowContext.getNames()) {
            var value = flowContext.get(name);
            scripts.append("var ").append(name).append(" = ").append(JSON.stringify(value)).append(";\n");
        }
        scripts.append("JSON.stringify(").append(expression).append(")");
        return scripts.toString();
    }
}
