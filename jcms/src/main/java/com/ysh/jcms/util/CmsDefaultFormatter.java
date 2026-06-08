package com.ysh.jcms.util;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;
import com.ysh.jcms.ffi.CmsType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认格式化器：
 * <ul>
 *   <li>标量（只有一个 {@code value} 字段）→ 单行 {@code (Type) value}
 *   <li>复合 → 多行缩进 {@code (Type) {\n    field: ...\n}}
 *   <li>JNA Union 字段 → 只显示名称和类型
 * </ul>
 */
public class CmsDefaultFormatter implements CmsToString {

    public static final CmsDefaultFormatter INSTANCE = new CmsDefaultFormatter();

    @Override
    public String toString(CmsType obj, int indent) {
        String pad = repeat("    ", indent);
        String simpleName = simpleName(obj.getClass());
        List<FieldEntry> entries = collectFields(obj);
        // 标量
        if (entries.size() == 1 && "value".equals(entries.get(0).name)) {
            return "(" + simpleName + ") " + formatValue(entries.get(0).value, indent);
        }
        // 复合
        StringBuilder sb = new StringBuilder("(").append(simpleName).append(") {");
        for (FieldEntry e : entries) {
            sb.append("\n").append(pad).append("    ").append(e.name).append(": ").append(formatValue(e.value, indent + 1));
        }
        sb.append("\n").append(pad).append("}");
        return sb.toString();
    }

    // ==================== 值格式化 ====================

    public static String formatValue(Object v, int indent) {
        if (v == null) return "null";
        if (v instanceof CmsType) return ((CmsType) v).formatter().toString((CmsType) v, indent);
        if (v instanceof Union) return formatUnion((Union) v);
        if (v instanceof byte[]) return byteArrayToString((byte[]) v);
        if (v instanceof Boolean || v instanceof Number) return v.toString();
        if (v instanceof Pointer)
            return "ptr@" + Long.toHexString(Pointer.nativeValue((Pointer) v));
        String s = v.toString();
        return s.length() > 120 ? s.substring(0, 120) + "…" : s;
    }

    private static String formatUnion(Union u) {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(simpleName(u.getClass())).append(") {");
        boolean found = false;
        Class<?> clazz = u.getClass();
        while (clazz != null && clazz != Union.class) {
            for (Field f : clazz.getDeclaredFields()) {
                if (Modifier.isStatic(f.getModifiers())) continue;
                String fn = f.getName();
                if ("_fieldOrder".equals(fn) || "fieldOrder".equals(fn) || "typeInfo".equals(fn)) continue;
                try {
                    Object val = f.get(u);
                    if (val == null) continue;
                    sb.append("\n    ").append(fn).append(": (").append(simpleName(val.getClass())).append(")");
                    found = true;
                } catch (Exception ignored) {}
            }
            clazz = clazz.getSuperclass();
        }
        if (!found) sb.append(" …");
        sb.append("\n}");
        return sb.toString();
    }

    static String byteArrayToString(byte[] data) {
        if (data == null) return "null";
        StringBuilder sb = new StringBuilder("\"");
        for (int i = 0; i < Math.min(data.length, 64); i++) {
            char c = (char) (data[i] & 0xFF);
            sb.append(c >= 32 && c < 127 ? c : '.');
        }
        sb.append('"');
        if (data.length > 64) sb.append("…");
        return sb.toString();
    }

    // ==================== 工具 ====================

    static String simpleName(Class<?> c) {
        String n = c.getSimpleName();
        if ("ByValue".equals(n) && c.getEnclosingClass() != null)
            n = c.getEnclosingClass().getSimpleName();
        return n;
    }

    public static String repeat(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }

    static List<FieldEntry> collectFields(CmsType obj) {
        List<FieldEntry> list = new ArrayList<>();
        Class<?> clazz = obj.getClass();
        while (clazz != null && clazz != CmsType.class) {
            for (Field f : clazz.getDeclaredFields()) {
                int mod = f.getModifiers();
                if (Modifier.isStatic(mod)) continue;
                String fn = f.getName();
                if ("name".equals(fn) || "encodeFn".equals(fn) || "decodeFn".equals(fn)
                    || "codecEnabled".equals(fn) || "formatter".equals(fn)) continue;
                try {
                    list.add(new FieldEntry(fn, f.get(obj)));
                } catch (Exception ignored) {}
            }
            clazz = clazz.getSuperclass();
        }
        return list;
    }

    static class FieldEntry {
        final String name;
        final Object value;
        FieldEntry(String name, Object value) { this.name = name; this.value = value; }
    }
}
