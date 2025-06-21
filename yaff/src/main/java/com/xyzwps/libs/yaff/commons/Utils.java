package com.xyzwps.libs.yaff.commons;

import java.util.regex.Pattern;

public final class Utils {

    public static final Pattern IDENTIFIER = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");

    public static boolean isIdentifier(String name) {
        return IDENTIFIER.matcher(name).matches();
    }

}
