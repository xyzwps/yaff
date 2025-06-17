package com.xyzwps.libs.yaff;

import lombok.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@NoArgsConstructor
@Data
public class FlowNode {

    private String id;
    private String name;
    private List<String> next; // 只有特定的节点才可以有多个 next
    private List<AssignExpression> assignExpressions;

    public static final Pattern ID_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");

    public void setId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }

        if (!ID_PATTERN.matcher(id).matches()) {
            throw new IllegalArgumentException("Invalid id: " + id);
        }

        this.id = id;
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
