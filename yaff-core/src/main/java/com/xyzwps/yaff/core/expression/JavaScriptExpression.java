package com.xyzwps.yaff.core.expression;

import com.xyzwps.yaff.core.FlowContext;
import com.xyzwps.yaff.core.SimpleFlowContext;
import com.xyzwps.yaff.core.commons.JSON;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

@Data
@NoArgsConstructor
public final class JavaScriptExpression implements Expression {

    public static final String TYPE = "javascript";

    private String expression;

    public JavaScriptExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public <T> T calculate(FlowContext flowContext, Class<T> resultType) {
        return calculate(expression, flowContext, resultType);
    }

    public static <T> T calculate(String expression, FlowContext flowContext, Class<T> resultType) {
        try (var context = Context.create()) {
            var script = makeScript(flowContext, expression);
            var value = context.eval("js", script);
            return convert(value, resultType);
        }
    }

    private static <T> T convert(Value value, Class<T> resultType) {
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
