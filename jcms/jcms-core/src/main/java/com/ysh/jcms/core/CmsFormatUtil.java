package com.ysh.jcms.core;

import com.ysh.jcms.data.string.CmsUint8Array;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Static helpers for formatting CmsType trees to string.
 */
public class CmsFormatUtil {

    private CmsFormatUtil() {}

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
            // Detect CHOICE: first child is CmsEnumerated (the selector)
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

    /** Check if children look like a CHOICE (first child is CmsEnumerated). */
    private static boolean isChoice(List<? extends CmsType> kids) {
        return !kids.isEmpty() && kids.get(0) instanceof CmsEnumerated;
    }

    /**
     * Render a CHOICE: only the selected alternative.
     * kids = [choice, alt0, alt1, ...]
     */
    private static String choiceToString(
            CmsType type, List<? extends CmsType> kids,
            int depth, Map<CmsType, String> fieldNames) {
        CmsEnumerated choice = (CmsEnumerated) kids.get(0);
        int idx = 1 + choice.value();  // alt starts at index 1
        CmsType selected = (idx >= 1 && idx < kids.size()) ? kids.get(idx) : null;
        String val = (selected != null)
            ? toString(selected, depth, fieldNames)
            : "(null)";
        return "(" + type.getClass().getSimpleName() + ") {CHOICE {" + val + "}}";
    }

    // ==================== Container ====================

    private static String containerToString(
            CmsType type, List<? extends CmsType> kids,
            int depth, Map<CmsType, String> fieldNames) {
        String indent = repeat("    ", depth + 1);
        String bracketIndent = repeat("    ", depth);
        StringBuilder sb = new StringBuilder()
            .append("(").append(type.getClass().getSimpleName()).append(") {\n");
        for (int i = 0; i < kids.size(); i++) {
            CmsType child = kids.get(i);
            String name = (fieldNames != null)
                ? fieldNames.getOrDefault(child, "[" + i + "]")
                : "[" + i + "]";
            // Skip duplicate index when name is already a positional tag
            boolean skipPos = name.startsWith("[");
            sb.append(indent);
            if (!skipPos) sb.append("[").append(i).append("] ");
            sb.append(name).append(": ");
            String val = (child != null)
                ? toString(child, depth + 1, fieldNames)
                : "(null)";
            sb.append(val).append(",\n");
        }
        if (!kids.isEmpty()) {
            sb.setLength(sb.length() - 2);
            sb.append("\n");
        }
        sb.append(bracketIndent).append("}");
        return sb.toString();
    }

    // ==================== CmsUint8Array ====================

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

    // ==================== Scalar ====================

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
}
