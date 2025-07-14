package com.xyzwps.yaff.core.demo;

import com.xyzwps.yaff.core.commons.JSON;
import lombok.Data;

public class DemoJsonSchema {
    public static void main(String[] args) {
        {
            var schema = JSON.toJsonSchema(Entity.class);
            System.out.println(JSON.pretty(schema));
        }
        {
            var schema = JSON.toJsonSchema(String.class);
            System.out.println(JSON.pretty(schema));
        }
        {
            var schema = JSON.toJsonSchema(Thread.State.class);
            System.out.println(JSON.pretty(schema));
        }
        {
            var schema = JSON.toJsonSchema(boolean.class);
            System.out.println(JSON.pretty(schema));
        }
    }

    @Data
    public static class Entity {
        public String name;
        public int age;
    }
}
