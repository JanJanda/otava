package io.github.janjanda.otava.library.utils;

public final class StringUtils {
    private StringUtils() {}

    public static String escapeTurtleString(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
