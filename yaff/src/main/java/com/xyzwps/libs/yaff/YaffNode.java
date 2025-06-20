package com.xyzwps.libs.yaff;

import java.util.List;

public interface YaffNode {

    String NOOP_NODE_NAME = "yaff.noop";

    String RNG_NODE_NAME = "yaff.rng";

    Node noopNode = Node.builder()
            .name(NOOP_NODE_NAME)
            .description("Noop node")
            .build();

    Node rngNode = Node.builder()
            .name(RNG_NODE_NAME)
            .description("生成一个随机数")
            .inputs(new NodeInput("min", ParameterType.FLOAT),
                    new NodeInput("max", ParameterType.FLOAT))
            .output(new NodeOutput(ParameterType.FLOAT))
            .execute((inputs) -> {
                var min = (Double) inputs.get("min");
                var max = (Double) inputs.get("max");
                return (Math.random() * (max - min) + min);
            }).build();
}
