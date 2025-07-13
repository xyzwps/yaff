package com.xyzwps.yaff.core;

public interface FlowExecutorListener {

    FlowExecutorListener NOOP = footPrint -> {
    };

    void onFootPrint(FootPrint footPrint);
}
