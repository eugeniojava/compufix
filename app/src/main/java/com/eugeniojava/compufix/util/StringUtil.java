package com.eugeniojava.compufix.util;

public class StringUtil {

    private StringUtil() {
    }

    public static boolean isEmpty(String string) {
        return string == null || string.trim().length() == 0;
    }
}
