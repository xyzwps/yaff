package com.xyzwps.libs.yaff.flow;

import lombok.*;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@Data
public class FlowNode {
    private String id;
    private String name;
    private List<String> next; // 只有特定的节点才可以有多个 next
    private List<AssignExpression> assignExpressions;


    public FlowNode id(String id) {
        this.id = id;
        return this;
    }

    public FlowNode name(String name) {
        this.name = name;
        return this;
    }

    public FlowNode assignExpressions(AssignExpression... expressions) {
        this.assignExpressions = Arrays.asList(expressions);
        return this;
    }

    public FlowNode next(List<String> next) {
        this.next = next;
        return this;
    }

    public FlowNode next(String... next) {
        this.next = Arrays.asList(next);
        return this;
    }
}
