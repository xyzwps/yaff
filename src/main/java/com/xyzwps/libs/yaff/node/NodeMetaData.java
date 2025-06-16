package com.xyzwps.libs.yaff.node;

import java.util.List;

public record NodeMetaData(String name, List<Parameter> input, List<Parameter> output) {
}
