package com.github.braisdom.funcsql.util;

public final class StringUtil {

    public static boolean isBlank(String string) {
        return string == null || string.trim().length() == 0;
    }

    public static String encodeExceptionMessage(Exception ex, String additio) {
        return String.format("%s: %s", additio, ex.getMessage());
    }
}
