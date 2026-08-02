package com.ysh.jcms.util;

/**
 * Static helpers for formatting output as JSON-safe strings.
 *
 * <p>Only {@link #escapeJson} is used by production code (jcms-app console output).
 * The former CmsType-tree renderers (toString/toJson) had no callers and were removed.
 */
public class CmsFormatUtil {

    private CmsFormatUtil() {}

    /** Escape a string for JSON (escape quotes, backslash, control chars). */
    public static String escapeJson(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"' :  sb.append("\\\""); break;
                case '\\' : sb.append("\\\\"); break;
                case '\b' : sb.append("\\b");  break;
                case '\f' : sb.append("\\f");  break;
                case '\n' : sb.append("\\n");  break;
                case '\r' : sb.append("\\r");  break;
                case '\t' : sb.append("\\t");  break;
                default :
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }
}
