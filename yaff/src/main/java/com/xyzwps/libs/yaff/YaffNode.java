package com.xyzwps.libs.yaff;

import java.util.List;

public interface YaffNode {

    String NOOP_NODE_NAME = "yaff.noop";

    Node noopNode = new NodeTemplate(NOOP_NODE_NAME, "Noop node", List.of(), List.of());
}
