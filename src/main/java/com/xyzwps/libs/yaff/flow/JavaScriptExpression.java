package com.xyzwps.libs.yaff.flow;

import com.xyzwps.libs.yaff.commons.JSON;
import com.xyzwps.libs.yaff.node.ParameterType;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.util.HashMap;

public class JavaScriptExpression implements AssignExpression {
    private final String expression;
    private final String inputName;

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
    public String getInputName() {
        return inputName;
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
        var names = flowContext.getNames();
        var allVars = new HashMap<String, HashMap<String, Object>>();
        for (var name : names) {
            var segments = name.split("\\.");
            if (segments.length != 2) {
                throw new RuntimeException("Invalid name: " + name);
            }

            var varName = segments[0];
            var fieldName = segments[1];
            if (!allVars.containsKey(varName)) {
                allVars.put(varName, new HashMap<>());
            }
            allVars.get(varName).put(fieldName, flowContext.get(name));
        }

        var scripts = new StringBuilder();

        for (var entry : allVars.entrySet()) {
            var varName = entry.getKey();
            var varValue = entry.getValue();
            scripts.append("var ").append(varName).append(" = ").append(JSON.stringify(varValue)).append(";\n");
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
