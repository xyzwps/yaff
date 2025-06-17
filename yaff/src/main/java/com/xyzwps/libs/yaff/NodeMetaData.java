package com.xyzwps.libs.yaff;

import java.util.List;

public record NodeMetaData(String name, List<Parameter> input, List<Parameter> output, String description) {
}
