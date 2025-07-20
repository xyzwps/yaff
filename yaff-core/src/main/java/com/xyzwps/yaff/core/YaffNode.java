package com.xyzwps.yaff.core;

public interface YaffNode {

    String NOOP_NODE_NAME = "yaff.noop";

    String RNG_NODE_NAME = "yaff.rng";

    Node noopNode = Node.builder()
            .name(NOOP_NODE_NAME)
            .description("一个不进行任何操作的节点。")
            .build();

    Node rngNode = Node.builder()
            .name(RNG_NODE_NAME)
            .description("生成一个随机数。")
            .inputs(new NodeInput("min", float.class),
                    new NodeInput("max", float.class))
            .output(new NodeOutput(float.class))
            .execute((inputs) -> {
                var min = ((Number) inputs.get("min")).doubleValue();
                var max = ((Number) inputs.get("max")).doubleValue();
                return (Math.random() * (max - min) + min);
            }).build();

    // TODO: 实现 countdown 节点
}
