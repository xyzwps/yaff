package com.xyzwps.libs.yaff.flow;

import lombok.*;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@Data
public class NodeInstance {
    private String id;
    private String name;
    private List<String> next; // 只有特定的节点才可以有多个 next
    private List<AssignExpression> assignExpressions;


    public NodeInstance id(String id) {
        this.id = id;
        return this;
    }

    public NodeInstance name(String name) {
        this.name = name;
        return this;
    }

    public NodeInstance assignExpressions(AssignExpression... expressions) {
        this.assignExpressions = Arrays.asList(expressions);
        return this;
    }

    public NodeInstance next(List<String> next) {
        this.next = next;
        return this;
    }

    public NodeInstance next(String... next) {
        this.next = Arrays.asList(next);
        return this;
    }
}
