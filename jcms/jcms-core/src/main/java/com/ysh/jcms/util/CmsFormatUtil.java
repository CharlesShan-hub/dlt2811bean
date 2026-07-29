package com.ysh.jcms.util;

import com.ysh.jcms.data.core.CmsChoice;
import com.ysh.jcms.data.core.CmsType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

/**
 * Static helpers for formatting {@link CmsType} trees to string or JSON.
 *
 * <p>Works with the new {@code CmsType} / {@code CmsChoice} / {@code CmsSequence}
 * hierarchy via reflection — no dependency on the old {@code CmsTypeOld} system.
 */
public class CmsFormatUtil {

    private CmsFormatUtil() {}

    // ==================== toString (YAML-like debug output) ====================

    /**
     * Render a CmsType subtree to a multi-line debug string.
     * Delegates to {@link CmsType#toString()}.
     */
    public static String toString(CmsType type) {
        return type.toString();
    }

    // ==================== toJson (machine-readable JSON output) ====================

    /**
     * Render a CmsType subtree as a compact JSON string.
     */
    public static String toJson(CmsType type) {
        StringBuilder sb = new StringBuilder();
        toJson(type, sb);
        return sb.toString();
    }

    private static void toJson(CmsType type, StringBuilder sb) {
        if (type == null) { sb.append("null"); return; }

        // CmsChoice → output only the selected variant value
        if (type instanceof CmsChoice) {
            choiceToJson((CmsChoice) type, sb);
            return;
        }

        // CmsSequence / other container → JSON object
        java.util.Map<String, Object> fields = collectFields(type);
        if (!fields.isEmpty()) {
            sb.append('{');
            boolean first = true;
            for (Map.Entry<String, Object> e : fields.entrySet()) {
                if (!first) sb.append(',');
                first = false;
                sb.append('"').append(escapeJson(e.getKey())).append("\":");
                toJsonValue(e.getValue(), sb);
            }
            sb.append('}');
            return;
        }

        // Leaf: scalar via innerCache / inner field
        scalarToJson(type, sb);
    }

    private static void choiceToJson(CmsChoice choice, StringBuilder sb) {
        int ch = choice.choice();
        if (ch < 0) { sb.append("null"); return; }

        // Find the selected variant field via reflection
        for (Field f : choice.getClass().getFields()) {
            if (Modifier.isStatic(f.getModifiers())) continue;
            // Look for @Choice annotation matching the current choice index
            CmsChoice.Choice ann = f.getAnnotation(CmsChoice.Choice.class);
            if (ann == null || ann.index() != ch) continue;
            try {
                Object val = f.get(choice);
                toJsonValue(val, sb);
                return;
            } catch (Exception e) {
                sb.append("null");
                return;
            }
        }

        // Fallback: no matching @Choice field (e.g. NULL variant)
        sb.append("null");
    }

    @SuppressWarnings("unchecked")
    private static void toJsonValue(Object val, StringBuilder sb) {
        if (val == null) { sb.append("null"); return; }
        if (val instanceof CmsType) { toJson((CmsType) val, sb); return; }
        if (val instanceof byte[]) { sb.append('"').append(escapeJson(bytesToHex((byte[]) val))).append('"'); return; }
        if (val instanceof String) { sb.append('"').append(escapeJson((String) val)).append('"'); return; }
        if (val instanceof Boolean || val instanceof Number) { sb.append(val); return; }
        if (val instanceof Collection) {
            sb.append('[');
            boolean first = true;
            for (Object item : (Collection<Object>) val) {
                if (!first) sb.append(',');
                first = false;
                toJsonValue(item, sb);
            }
            sb.append(']');
            return;
        }
        // Fallback
        sb.append('"').append(escapeJson(val.toString())).append('"');
    }

    private static java.util.Map<String, Object> collectFields(CmsType type) {
        java.util.Map<String, Object> fields = new java.util.LinkedHashMap<>();
        for (Field f : type.getClass().getFields()) {
            if (Modifier.isStatic(f.getModifiers())) continue;
            String name = f.getName();
            if ("inner".equals(name) || "innerCache".equals(name)) continue;
            try {
                Object val = f.get(type);
                if (val != null) fields.put(name, val);
            } catch (Exception ignored) {}
        }
        return fields;
    }

    private static void scalarToJson(CmsType type, StringBuilder sb) {
        // Try innerCache["value"] first (CmsScalar pattern)
        Object cached = type.innerCache.get("value");
        if (cached != null) {
            toJsonValue(cached, sb);
            return;
        }
        // Fallback: low-level scalar → read from inner's "value" field
        try {
            Field vf = type.inner.getClass().getField("value");
            Object val = vf.get(type.inner);
            toJsonValue(val, sb);
        } catch (Exception e) {
            sb.append("null");
        }
    }

    // ==================== Helpers ====================

    private static boolean isPrintable(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < 0x20 && c != '\t' && c != '\n' && c != '\r')
                return false;
            if (c == 0xFFFD)
                return false;
        }
        return true;
    }

    static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes)
            sb.append(String.format("%02x", b & 0xFF));
        return sb.toString();
    }

    static String repeat(String s, int count) {
        StringBuilder sb = new StringBuilder(s.length() * count);
        for (int i = 0; i < count; i++)
            sb.append(s);
        return sb.toString();
    }

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
