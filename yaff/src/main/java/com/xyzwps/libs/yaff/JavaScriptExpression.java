package com.xyzwps.libs.yaff;

import com.xyzwps.libs.yaff.commons.JSON;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

@Data
@NoArgsConstructor
public final class JavaScriptExpression implements AssignExpression {
    private String expression;
    private String inputName;

    public static final String TYPE = "javascript";

    public JavaScriptExpression(String name, String expression) {
        this.inputName = name;
        this.expression = expression;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public Object calculate(FlowContext flowContext, ParameterType resultType) {
        try (var context = Context.create()) {
            var script = makeScript(flowContext) + expression;
            var value = context.eval("js", script);
            return convert(value, resultType);
        }
    }


    /**
     * 把 flowContext 转换成 JavaScript 脚本。
     *
     * @param flowContext 上下文
     * @return 脚本
     * @see SimpleFlowContext#set
     */
    private static String makeScript(FlowContext flowContext) {
        var scripts = new StringBuilder();
        for (var name : flowContext.getNames()) {
            var value = flowContext.get(name);
            scripts.append("var ").append(name).append(" = ").append(JSON.stringify(value)).append(";\n");
        }
        return scripts.toString();
    }


    private static Object convert(Value value, ParameterType resultType) {
        switch (resultType) {
            case INT -> {
                if (value.isNumber()) {
                    return value.asInt();
                } else {
                    throw new RuntimeException("Not a number");
                }
            }
            case FLOAT -> {
                if (value.isNumber()) {
                    return value.asDouble();
                } else {
                    throw new RuntimeException("Not a number");
                }
            }
            case STRING -> {
                if (value.isString()) {
                    return value.asString();
                } else {
                    throw new RuntimeException("Not a string");
                }
            }
            case BOOL -> {
                if (value.isBoolean()) {
                    return value.asBoolean();
                } else {
                    throw new RuntimeException("Not a boolean");
                }
            }
        }
        throw new RuntimeException("Unhandled parameter type: " + resultType);
    }
}
