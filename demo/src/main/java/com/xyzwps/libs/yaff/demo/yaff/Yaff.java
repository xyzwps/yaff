package com.xyzwps.libs.yaff.demo.yaff;

import com.xyzwps.libs.yaff.FlowFactory;
import com.xyzwps.libs.yaff.NodeMetaData;

import java.util.List;

public class Yaff {

    public static final FlowFactory FACTORY = new FlowFactory();

    static {
        FACTORY.register(new SendMessageNode());
        FACTORY.register(new RNGNode());
    }

    public static List<NodeMetaData> getMetaData() {
        return FACTORY.getNodeRegister().getMetaData();
    }
}
