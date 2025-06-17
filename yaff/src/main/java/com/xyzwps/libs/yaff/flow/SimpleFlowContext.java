package com.xyzwps.libs.yaff.flow;

import com.xyzwps.libs.yaff.commons.NodeIds;

import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;

public class SimpleFlowContext implements FlowContext {
    private final HashMap<String, Object> map = new HashMap<>();

    static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");
    static final Pattern PATH_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*\\.[a-zA-Z_][a-zA-Z0-9_]*$");

    /**
     * 我们只保留两级名称。第一级是 node id，第二级是 node 输出的变量名。
     * <p/>
     * 如果名称只有一级，我们把它放在 {@link NodeIds#CTX} 虚拟节点下，即，变量名是 {@link NodeIds#CTX}.name。
     * 如果名称有两级，直接接受。
     * 否则，抛出异常。
     *
     * @param name  Variable Name
     * @param value Variable Value TODO: 限制值域
     */
    @Override
    public void set(String name, Object value) {
        if (name == null) {
            throw new IllegalArgumentException("Context variable name cannot be null");
        }

        if (PATH_PATTERN.matcher(name).matches()) {
            map.put(name, value);
        } else if (NAME_PATTERN.matcher(name).matches()) {
            map.put(NodeIds.CTX + "." + name, value);
        } else {
            throw new IllegalArgumentException("Invalid context variable name: " + name);
        }
    }

    @Override
    public Object get(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Context variable path cannot be null");
        }
        if (!PATH_PATTERN.matcher(path).matches()) {
            throw new IllegalArgumentException("Invalid context variable path: " + path);
        }
        return map.get(path);
    }

    @Override
    public Set<String> getNames() {
        return map.keySet();
    }
}
