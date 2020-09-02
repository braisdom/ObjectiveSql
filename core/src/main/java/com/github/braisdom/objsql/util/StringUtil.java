package com.github.braisdom.objsql.util;

public final class StringUtil {

    public static boolean isBlank(String string) {
        return string == null || string.trim().length() == 0;
    }

    public static String encodeExceptionMessage(Exception ex, String addition) {
        return String.format("%s: %s", addition, ex.getMessage());
    }

    public static String[] splitNameOf(Class<?> type) {
        return type.getName().split("\\.");
    }

    public static String truncate(String str, int length) {
        assert length >= 3 : length;
        length = length - 3;
        if (str.length() <= length)
            return str;
        else
            return str.substring(0, length) + "...";
    }
}
