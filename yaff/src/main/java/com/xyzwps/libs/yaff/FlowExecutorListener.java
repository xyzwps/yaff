package com.xyzwps.libs.yaff;

public interface FlowExecutorListener {

    FlowExecutorListener NOOP = footPrint -> {
    };

    void onFootPrint(FootPrint footPrint);
}
