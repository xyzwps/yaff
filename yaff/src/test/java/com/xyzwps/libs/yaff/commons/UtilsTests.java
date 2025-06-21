package com.xyzwps.libs.yaff.commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTests {

    @Test
    void testIsIdentifier() {
        var positiveCases = new String[]{
                "a", "A", "_",
                "b1", "b_", "bb", "bB",
                "_1", "__", "_b", "_B",
                "B1", "B_", "Bb", "BB",
                "amazing_KE_Q1ng"
        };
        for (var c : positiveCases) {
            assertTrue(Utils.isIdentifier(c));
        }

        var negativeCases = new String[]{"1", "1a", "1_", "amazing_刻晴", " abc"};
        for (var c : negativeCases) {
            assertFalse(Utils.isIdentifier(c));
        }
    }
}
