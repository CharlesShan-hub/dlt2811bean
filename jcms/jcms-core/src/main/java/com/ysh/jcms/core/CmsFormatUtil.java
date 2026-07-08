package com.ysh.jcms.core;

import com.ysh.jcms.data.string.CmsUint8Array;
import java.nio.charset.StandardCharsets;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Static helpers for formatting CmsType trees to string or JSON.
 */
public class CmsFormatUtil {

    private CmsFormatUtil() {}

    // ==================== toString (YAML-like debug output) ====================

    /**
     * Render a CmsType subtree to a multi-line debug string.
     *
     * @param type        the type to render
     * @param depth       indentation depth (0 = root)
     * @param fieldNames  optional mapping from CmsType pointer -> field name (can be null)
     */
    public static String toString(CmsType type, int depth, Map<CmsType, String> fieldNames) {
        List<? extends CmsType> kids = type.children();

        if (!kids.isEmpty()) {
            if (isChoice(kids)) {
                return choiceToString(type, kids, depth, fieldNames);
            }
            return containerToString(type, kids, depth, fieldNames);
        }
        if (type instanceof CmsUint8Array) {
            return uint8ArrayToString((CmsUint8Array) type);
        }
        return scalarToString(type);
    }

    // ==================== toJson (machine-readable JSON output) ====================

    /**
     * Render a CmsType subtree as a compact JSON string.
     */
    public static String toJson(CmsType type) {
        Map<CmsType, String> fieldNames = buildFieldNameMap(type, type.children());
        StringBuilder sb = new StringBuilder();
        toJson(type, fieldNames, sb);
        return sb.toString();
    }

    private static void toJson(CmsType type, Map<CmsType, String> fieldNames, StringBuilder sb) {
        List<? extends CmsType> kids = type.children();

        // CmsArray → JSON array
        if (type instanceof CmsArray) {
            CmsArray<?> arr = (CmsArray<?>) type;
            sb.append('[');
            for (int i = 0; i < arr.items.size(); i++) {
                if (i > 0) sb.append(',');
                toJson(arr.items.get(i), fieldNames, sb);
            }
            sb.append(']');
            return;
        }

        if (!kids.isEmpty()) {
            if (isChoice(kids)) {
                // CHOICE → only the selected alternative
                CmsEnumerated choice = (CmsEnumerated) kids.get(0);
                int idx = 1 + choice.value();
                CmsType selected = (idx >= 1 && idx < kids.size()) ? kids.get(idx) : null;
                if (selected != null) {
                    toJson(selected, fieldNames, sb);
                } else {
                    sb.append("null");
                }
            } else {
                // Container → JSON object
                sb.append('{');
                int count = 0;
                for (int i = 0; i < kids.size(); i++) {
                    CmsType child = kids.get(i);
                    if (child == null) continue;
                    String name = fieldNames != null
                        ? fieldNames.getOrDefault(child, "field" + i)
                        : "field" + i;
                    if (count > 0) sb.append(',');
                    sb.append('"').append(escapeJson(name)).append("\":");
                    toJson(child, fieldNames, sb);
                    count++;
                }
                sb.append('}');
            }
            return;
        }

        // Leaf: CmsUint8Array → JSON string
        if (type instanceof CmsUint8Array) {
            uint8ArrayToJson((CmsUint8Array) type, sb);
            return;
        }

        // Leaf: scalar → number or boolean
        scalarToJson(type, sb);
    }

    private static void uint8ArrayToJson(CmsUint8Array arr, StringBuilder sb) {
        byte[] data = arr.value();
        if (arr.len > 0 && data.length > 0) {
            String s = new String(data, StandardCharsets.UTF_8);
            if (isPrintable(s)) {
                sb.append('"').append(escapeJson(s)).append('"');
            } else {
                sb.append('"').append("hex:").append(bytesToHex(data)).append('"');
            }
        } else {
            sb.append("\"\"");
        }
    }

    private static void scalarToJson(CmsType type, StringBuilder sb) {
        String name = type.getClass().getSimpleName();
        long val;
        switch (type.nativeSize) {
            case 1: val = type.nativePtr.getByte(0); break;
            case 2: val = type.nativePtr.getShort(0); break;
            case 4: val = type.nativePtr.getInt(0); break;
            case 8: val = type.nativePtr.getLong(0); break;
            default: sb.append("null"); return;
        }
        // CmsBoolean → true/false
        if ("CmsBoolean".equals(name) || "CmsBOOLEAN".equals(name)) {
            sb.append(val != 0 ? "true" : "false");
        } else {
            sb.append(val);
        }
    }

    // ==================== Field name resolution ====================

    /**
     * Build a field name map for a CmsType and its subtree.
     * Recursively walks children to find all reachable CmsType instances.
     */
    private static Map<CmsType, String> buildFieldNameMap(CmsType type, List<? extends CmsType> directChildren) {
        Map<CmsType, String> map = new IdentityHashMap<>();
        collectFieldNames(type, map);
        return map;
    }

    private static void collectFieldNames(CmsType type, Map<CmsType, String> map) {
        if (type == null || type.getClass() == CmsType.class) return;
        for (java.lang.reflect.Field f : type.getClass().getFields()) {
            if (CmsType.class.isAssignableFrom(f.getType())) {
                try {
                    Object v = f.get(type);
                    if (v != null && v instanceof CmsType) {
                        map.put((CmsType) v, f.getName());
                    }
                } catch (Exception ignored) {}
            }
        }
        // Recurse through children for nested fields
        for (CmsType child : type.children()) {
            if (child != null && child != type) {
                collectFieldNames(child, map);
            }
        }
    }

    // ==================== Existing toString methods (unchanged) ====================

    private static boolean isChoice(List<? extends CmsType> kids) {
        return !kids.isEmpty() && kids.get(0) instanceof CmsEnumerated;
    }

    private static String choiceToString(
            CmsType type, List<? extends CmsType> kids,
            int depth, Map<CmsType, String> fieldNames) {
        CmsEnumerated choice = (CmsEnumerated) kids.get(0);
        int idx = 1 + choice.value();
        CmsType selected = (idx >= 1 && idx < kids.size()) ? kids.get(idx) : null;
        String val = (selected != null)
            ? toString(selected, depth, fieldNames)
            : "(null)";
        return "(" + type.getClass().getSimpleName() + ") " + val;
    }

    private static String containerToString(
            CmsType type, List<? extends CmsType> kids,
            int depth, Map<CmsType, String> fieldNames) {
        String indent = repeat("    ", depth + 1);
        StringBuilder sb = new StringBuilder()
            .append("(").append(type.getClass().getSimpleName()).append(")\n");
        for (int i = 0; i < kids.size(); i++) {
            CmsType child = kids.get(i);
            String name = (fieldNames != null)
                ? fieldNames.getOrDefault(child, "[" + i + "]")
                : "[" + i + "]";
            boolean skipPos = name.startsWith("[");
            sb.append(indent);
            if (!skipPos) sb.append("[").append(i).append("] ");
            sb.append(name).append(": ");
            String val = (child != null)
                ? toString(child, depth + 1, fieldNames)
                : "(null)";
            sb.append(val).append("\n");
        }
        if (!kids.isEmpty()) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    private static String uint8ArrayToString(CmsUint8Array arr) {
        String prefix = "(" + arr.getClass().getSimpleName() + ") ";
        byte[] data = arr.value();
        if (arr.len > 0 && data.length > 0) {
            String s = new String(data, StandardCharsets.UTF_8);
            if (isPrintable(s)) {
                return prefix + "'" + s + "'";
            }
            return prefix + "hex:" + bytesToHex(data);
        }
        return prefix + "(empty)";
    }

    private static String scalarToString(CmsType type) {
        long val = 0;
        switch (type.nativeSize) {
            case 1: val = type.nativePtr.getByte(0); break;
            case 2: val = type.nativePtr.getShort(0); break;
            case 4: val = type.nativePtr.getInt(0); break;
            case 8: val = type.nativePtr.getLong(0); break;
        }
        return "(" + type.getClass().getSimpleName() + ") " + val;
    }

    // ==================== Helpers ====================

    private static boolean isPrintable(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < 0x20 && c != '\t' && c != '\n' && c != '\r') return false;
            if (c == 0xFFFD) return false;
        }
        return true;
    }

    static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) sb.append(String.format("%02x", b & 0xFF));
        return sb.toString();
    }

    static String repeat(String s, int count) {
        StringBuilder sb = new StringBuilder(s.length() * count);
        for (int i = 0; i < count; i++) sb.append(s);
        return sb.toString();
    }

    /** Escape a string for JSON (escape quotes, backslash, control chars). */
    public static String escapeJson(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
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
