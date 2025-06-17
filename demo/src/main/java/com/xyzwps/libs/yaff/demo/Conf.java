package com.xyzwps.libs.yaff.demo;

import io.helidon.config.Config;

public class Conf {

    public static final Config config = Config.create();

    public static Config get(String name) {
        return config.get(name);
    }
}
