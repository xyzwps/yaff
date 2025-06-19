package com.xyzwps.libs.yaff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@NoArgsConstructor
@Data
public class FlowNode {

    /// 节点 id。在一个 {@link Flow} 中，每个节点的 id 是唯一的。
    ///
    /// - required: true
    private String id;

    /// 节点引用。用于后续节点访问本节点输出的结果。
    ///
    /// - required: false
    private String ref;

    /// 节点要做之事的文字描述。
    ///
    /// - required: false
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    /// 节点在画布上的 x 坐标。
    ///
    /// - required: false
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Float px;

    /// 节点在画布上的 y 坐标。
    ///
    /// - required: false
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Float py;

    /// 节点的对应的 {@link Node} 的名字
    ///
    /// - required: true
    private String name;

    /// 节点的下一跳节点列表。
    ///
    /// 一个节点后可以没有后续节点。
    /// 一般而言，一个节点后续节点最多只有一个。
    /// 只有内置的特定的节点才可以有多个后续节点。
    ///
    /// - required: false
    private List<String> next;

    /// 为节点输入参数赋值的表达式列表。
    ///
    /// - required: true
    private List<AssignExpression> assignExpressions;

    public static final Pattern ID_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");

    private static void checkId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }

        if (!ID_PATTERN.matcher(id).matches()) {
            throw new IllegalArgumentException("Invalid id: " + id);
        }
    }

    private static void checkRef(String ref) {
        if (ref == null) {
            return;
        }

        if (!ID_PATTERN.matcher(ref).matches()) {
            throw new IllegalArgumentException("Invalid ref: " + ref);
        }
    }

    public void setRef(String ref) {
        checkRef(ref);
        this.ref = ref;
    }

    public void setId(String id) {
        checkId(id);
        this.id = id;
    }

    public FlowNode ref(String ref) {
        this.setRef(ref);
        return this;
    }

    public FlowNode id(String id) {
        this.setId(id);
        return this;
    }

    public FlowNode name(String name) {
        this.name = name;
        return this;
    }

    public FlowNode assignExpressions(AssignExpression... expressions) {
        for (var expression : expressions) {
            if (expression == null) {
                throw new IllegalArgumentException("expression cannot be null");
            }
        }

        this.assignExpressions = Arrays.asList(expressions);
        return this;
    }

    public FlowNode next(String... next) {
        for (var n : next) {
            if (n == null) {
                throw new IllegalArgumentException("next cannot be null");
            }
            if (!ID_PATTERN.matcher(n).matches()) {
                throw new IllegalArgumentException("Invalid next: " + n);
            }
        }

        this.next = Arrays.asList(next);
        return this;
    }
}
