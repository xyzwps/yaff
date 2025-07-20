package com.xyzwps.yaff.core.commons;

import java.util.function.BooleanSupplier;

public final class PM {

    public static void run(RBranch... branches) {
        for (var branch : branches) {
            if (branch.condition.getAsBoolean()) {
                branch.runnable.run();
                return;
            }
        }
    }

    public record RBranch(BooleanSupplier condition, Runnable runnable) {

        public static RBranch of(BooleanSupplier condition, Runnable runnable) {
            return new RBranch(condition, runnable);
        }

        private static final BooleanSupplier TRUE = () -> true;

        public static RBranch fallback(Runnable runnable) {
            return new RBranch(TRUE, runnable);
        }
    }


}
