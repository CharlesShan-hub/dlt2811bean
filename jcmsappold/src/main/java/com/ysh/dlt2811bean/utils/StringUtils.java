package com.ysh.dlt2811bean.utils;

public class StringUtils {

    private StringUtils() {}

    public static String hex(byte[] bytes) {
        if (bytes == null) return "null";
        StringBuilder sb = new StringBuilder();
        int len = Math.min(bytes.length, 8);
        for (int i = 0; i < len; i++) {
            sb.append(String.format("%02X", bytes[i]));
        }
        if (bytes.length > 8) sb.append("...");
        return sb.toString();
    }
}