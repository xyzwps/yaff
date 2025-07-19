package com.xyzwps.yaff.core;

import com.xyzwps.yaff.core.commons.Utils;
import com.xyzwps.yaff.core.expression.Expression;

public record FlowEdgeTo(String to, EdgeType type, Expression expression) {
    public boolean validated() {
        if (to == null || !Utils.isIdentifier(to)) {
            return false;
        }
        if (type == null) {
            return false;
        }
        return switch (type) {
            case CHECK -> expression != null;
            case FALLBACK -> true;
        };
    }

    public enum EdgeType {
        CHECK,
        FALLBACK
    }

    public static FlowEdgeTo fallback(String to) {
        return new FlowEdgeTo(to, EdgeType.FALLBACK, null);
    }

    public static FlowEdgeTo check(String to, Expression expression) {
        return new FlowEdgeTo(to, EdgeType.CHECK, expression);
    }

}
