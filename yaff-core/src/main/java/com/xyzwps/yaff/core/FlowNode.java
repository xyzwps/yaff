package com.xyzwps.yaff.core;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.xyzwps.yaff.core.expression.AssignExpression;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.xyzwps.yaff.core.commons.Utils.*;

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

    /// 从当前节点出发可以到达的边列表。
    ///
    /// - required: false
    private List<FlowEdgeTo> edges;

    /// 为节点输入参数赋值的表达式列表。
    ///
    /// - required: true
    private List<AssignExpression> assignExpressions;


    private static void checkId(String id) {
        if (id == null) {
            throw new YaffException("FlowNode id cannot be null.");
        }

        if (!isIdentifier(id)) {
            throw new YaffException("Invalid id: " + id);
        }
    }

    private static void checkRef(String ref) {
        if (ref == null) {
            return;
        }

        if (!isIdentifier(ref)) {
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

    public FlowNode edges(String id) {
        if (this.edges == null) {
            this.edges = new ArrayList<>();
        }
        this.edges.add(FlowEdgeTo.fallback(id));
        return this;
    }

    public FlowNode edges(FlowEdgeTo... edges) {
        for (var e : edges) {
            if (e == null) {
                throw new IllegalArgumentException("edge cannot be null");
            }
            if (!e.validated()) {
                throw new IllegalArgumentException("Invalid edge: " + e);
            }
        }

        if (this.edges == null) {
            this.edges = new ArrayList<>();
        }
        this.edges.addAll(Arrays.asList(edges));
        return this;
    }
}
