package com.ysh.jcms.ffi;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

@Getter
@Setter
@Accessors(fluent = true)
public abstract class CmsType extends Structure {

    @FunctionalInterface
    public interface CmsEncodeFn {
        int apply(Structure v, byte[] buf, IntByReference outLen);
    }

    @FunctionalInterface
    public interface CmsDecodeFn {
        void apply(Structure v, byte[] data, int len);
    }

    protected final String name;
    private final CmsEncodeFn encodeFn;
    private final CmsDecodeFn decodeFn;
    private final boolean codecEnabled;

    /** 默认构造 — 自动绑定 FFI encode/decode。 */
    protected CmsType() {
        this(true);
    }

    /**
     * 带开关构造。
     *
     * @param codecEnabled {@code true} 自动绑定 FFI encode/decode；{@code false} 跳过。
     */
    protected CmsType(boolean codecEnabled) {
        String cn = getClass().getSimpleName();
        // ByValue 内类——用外层类名，且不绑定 FFI（仅用于嵌入）
        if ("ByValue".equals(cn) && getClass().getEnclosingClass() != null) {
            cn = getClass().getEnclosingClass().getSimpleName();
            codecEnabled = false;
        }
        this.name = toCmsName(cn);
        this.codecEnabled = codecEnabled;
        if (codecEnabled) {
            this.encodeFn = findEncodeFn(cn, "encode");
            this.decodeFn = findDecodeFn(cn, "decode");
        } else {
            this.encodeFn = null;
            this.decodeFn = null;
        }
    }

    // ==================== 编码解码 ====================

    /** 是否有 encode/decode 能力。 */
    public boolean codecEnabled() { return codecEnabled; }

    /** 编码缓冲区大小提示，子类可重写。 */
    protected int encodeBufSize() { return 256; }

    /** 编码当前结构体为字节数组。自动 {@code write()} 同步 Java 字段到 native 内存。 */
    public byte[] encode() {
        if (!codecEnabled) throw new UnsupportedOperationException(name + " has no FFI encode");
        write();
        byte[] buf = new byte[encodeBufSize()];
        IntByReference outLen = new IntByReference(buf.length);
        encodeFn.apply(this, buf, outLen);
        return Arrays.copyOf(buf, outLen.getValue());
    }

    /** 从字节数组解码到当前结构体。自动 {@code read()} 同步 native 内存到 Java 字段。 */
    @SuppressWarnings("unchecked")
    public <T extends CmsType> T decode(byte[] data) {
        if (!codecEnabled) throw new UnsupportedOperationException(name + " has no FFI decode");
        decodeFn.apply(this, data, data.length);
        read();
        postDecode();
        return (T) this;
    }

    /** 解码后回调 — 子类可在此提取临时指针数据。自动遍历嵌套 Structure 字段递归调用。 */
    protected void postDecode() {
        for (java.lang.reflect.Field f : getClass().getFields()) {
            if (Structure.class.isAssignableFrom(f.getType())) {
                try {
                    Object o = f.get(this);
                    if (o instanceof CmsType) ((CmsType) o).postDecode();
                } catch (Exception e) { /* ignore */ }
            }
        }
    }

    // ==================== equals / hashCode ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !getClass().equals(o.getClass())) return false;
        try {
            for (String fn : getFieldOrder()) {
                java.lang.reflect.Field f = getClass().getField(fn);
                Object a = f.get(this);
                Object b = f.get(o);
                if (a instanceof com.sun.jna.Pointer && b instanceof com.sun.jna.Pointer) {
                    // Prefer bytes() content over Pointer address
                    try {
                        Method m = getClass().getMethod("bytes");
                        if (!Arrays.equals((byte[]) m.invoke(this), (byte[]) m.invoke(o))) {
                            return false;
                        }
                    } catch (NoSuchMethodException e) {
                        if (!Objects.equals(a, b)) return false;
                    }
                } else if (!Objects.equals(a, b)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int h = 0;
        try {
            for (String fn : getFieldOrder()) {
                java.lang.reflect.Field f = getClass().getField(fn);
                Object v = f.get(this);
                if (v instanceof com.sun.jna.Pointer) {
                    try {
                        Method m = getClass().getMethod("bytes");
                        v = m.invoke(this);
                    } catch (NoSuchMethodException ignored) {}
                }
                h = 31 * h + (v != null ? v.hashCode() : 0);
            }
        } catch (Exception ignored) {}
        return h;
    }

    // ==================== 名称 ====================

    public String name() { return name; }

    @Override
    public String toString() { return name; }

    // ==================== 私有工具 ====================

    /** 根据后缀（encode/decode）反射查找 CmsFFI 上的方法。 */
    private static String ffiMethodName(String className, String suffix) {
        String raw = className.startsWith("Cms") ? className.substring(3) : className;
        StringBuilder sb = new StringBuilder("cms_");
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            if (Character.isUpperCase(c) && i > 0 && i < raw.length() - 1
                    && Character.isLowerCase(raw.charAt(i + 1))) {
                sb.append('_');
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.append('_').append(suffix).toString();
    }

    private static CmsEncodeFn findEncodeFn(String cn, String suffix) {
        String mn = ffiMethodName(cn, suffix);
        try {
            Method m = CmsFFI.class.getMethod(mn, Structure.class, byte[].class, IntByReference.class);
            return (v, buf, outLen) -> {
                try { return (int) m.invoke(CmsFFI.INSTANCE, v, buf, outLen); }
                catch (Exception e) { throw new RuntimeException(e); }
            };
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("FFI encode method not found: " + mn, e);
        }
    }

    private static CmsDecodeFn findDecodeFn(String cn, String suffix) {
        String mn = ffiMethodName(cn, suffix);
        try {
            Method m = CmsFFI.class.getMethod(mn, Structure.class, byte[].class, int.class);
            return (v, data, len) -> {
                try { m.invoke(CmsFFI.INSTANCE, v, data, len); }
                catch (Exception e) { throw new RuntimeException(e); }
            };
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("FFI decode method not found: " + mn, e);
        }
    }

    /** "CmsBoolean" → "cms_boolean_t", "CmsInt16U" → "cms_int16u_t" */
    private static String toCmsName(String className) {
        String raw = className.startsWith("Cms") ? className.substring(3) : className;
        StringBuilder sb = new StringBuilder("cms_");
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            if (Character.isUpperCase(c) && i > 0 && i < raw.length() - 1
                    && Character.isLowerCase(raw.charAt(i + 1))) {
                sb.append('_');
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.append("_t").toString();
    }
}