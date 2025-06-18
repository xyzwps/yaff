package com.xyzwps.libs.yaff.example;

import com.xyzwps.libs.yaff.FlowFactory;

class Commons {
    static final FlowFactory factory = new FlowFactory()
            .register(new PrintTextNode())
            .register(new TextToUpperNode());
}
