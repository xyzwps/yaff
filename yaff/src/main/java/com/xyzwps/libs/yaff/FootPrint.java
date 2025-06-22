package com.xyzwps.libs.yaff;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FootPrint.BeforeNode.class, name = "before-node"),
        @JsonSubTypes.Type(value = FootPrint.InputsCalculated.class, name = "input-calculated"),
        @JsonSubTypes.Type(value = FootPrint.OutputExecuted.class, name = "output-executed"),
        @JsonSubTypes.Type(value = FootPrint.PutRefIntoContext.class, name = "put-ref-into-context"),
        @JsonSubTypes.Type(value = FootPrint.End.class, name = "end"),
        @JsonSubTypes.Type(value = FootPrint.BranchEnd.class, name = "branch-end"),
        @JsonSubTypes.Type(value = FootPrint.CheckWhenNode.class, name = "check-when-node"),
        @JsonSubTypes.Type(value = FootPrint.ToNext.class, name = "to-next")
})
public sealed interface FootPrint {

    record BeforeNode(String id, String name) implements FootPrint {
    }

    record InputsCalculated(String id, Map<String, Object> inputs) implements FootPrint {
    }

    record OutputExecuted(String id, Object output) implements FootPrint {
    }

    record PutRefIntoContext(String id, String ref, Object output) implements FootPrint {
    }

    record End() implements FootPrint {
    }

    record BranchEnd(String branchStartId) implements FootPrint {
    }

    record CheckWhenNode(String id) implements FootPrint {
    }

    record ToNext(String id, String next) implements FootPrint {
    }

    FootPrint END = new End();
}
