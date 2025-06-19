package com.xyzwps.libs.yaff;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public interface Node {
    default String getName() {
        return getMetaData().name();
    }

    default String getDescription() {
        return getMetaData().description();
    }

    default List<NodeInput> getInputs() {
        return getMetaData().inputs();
    }

    default NodeOutput getOutput() {
        return getMetaData().output();
    }

    NodeMetaData getMetaData();

    /// 根据输入的内容，执行节点任务，并返回结果。
    Object execute(Map<String, Object> inputs);

    static NodeBuilder builder() {
        return new NodeBuilder();
    }

    Pattern NODE_NAME_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)*$");
}
