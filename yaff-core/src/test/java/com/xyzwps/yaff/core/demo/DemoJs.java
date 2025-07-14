package com.xyzwps.yaff.core.demo;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.util.function.Consumer;

public class DemoJs {
    public static void main(String[] args) {
        System.setProperty("engine.WarnInterpreterOnly","false");
        exec("1", DemoJs::display);
        exec("null", DemoJs::display);
        exec("'xx'", DemoJs::display);
    }

    private static void display(Value value) {
        System.out.println(value);
    }

    private static void exec(String script, Consumer<Value> consumer) {
        try (var context = Context.create()) {
            consumer.accept(context.eval("js", script));
        }
    }
}
