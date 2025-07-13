package com.xyzwps.yaff.core;

import java.util.List;

public record NodeMetaData(String name,
                           String description,
                           List<NodeInput> inputs,
                           NodeOutput output) {
}
