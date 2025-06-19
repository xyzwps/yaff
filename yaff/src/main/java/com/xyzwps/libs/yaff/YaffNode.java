package com.xyzwps.libs.yaff;

import java.util.List;

public interface YaffNode {

    String NOOP_NODE_NAME = "yaff.noop";

    Node noopNode = Node.builder()
            .name(NOOP_NODE_NAME)
            .description("Noop node")
            .build();
}
