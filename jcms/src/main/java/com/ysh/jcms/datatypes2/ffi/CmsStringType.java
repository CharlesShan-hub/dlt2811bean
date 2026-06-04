package com.ysh.jcms.datatypes2.ffi;

import com.sun.jna.FromNativeContext;
import com.sun.jna.NativeMapped;
import com.sun.jna.ptr.IntByReference;

import java.nio.charset.StandardCharsets;

/**
 * 所有 String 类型的基础类。
 *
 * <p>实现 {@link NativeMapped}，可以直接作为 {@link com.sun.jna.Structure} 字段。
 *
 * <p>Fixed 版本规则：构造参数是 PER 最大长度 {@code N}，
 * JNA 自动分配 {@code N+1} 字节（+1 给 C 的 NUL 终止符），
 * 所以 {@code new CmsVisibleStringFixed(129)} 对应 C 的 {@code char[130]}。
 *
 * @param <T> 子类自身类型
 */
public abstract class CmsStringType<T extends CmsStringType<T>> implements NativeMapped {

    protected String value;
    /** PER 编码最大长度（不含 NUL）。同时 {@link #nativeSize()} = perMaxLen + 1。 */
    protected final int perMaxLen;

    protected CmsStringType(int perMaxLen) {
        this(perMaxLen, "");
    }

    protected CmsStringType(int perMaxLen, String value) {
        this.perMaxLen = perMaxLen;
        this.value = value != null ? value : "";
    }

    public String get() { return value; }
    public void set(String value) { this.value = value != null ? value : ""; }
    public int perMaxLen() { return perMaxLen; }

    // ==================== NativeMapped ====================

    @Override
    public Class<?> nativeType() {
        return byte[].class;
    }

    @Override
    public Object toNative() {
        byte[] bytes = value.getBytes(StandardCharsets.US_ASCII);
        byte[] result = new byte[perMaxLen + 1];
        int len = Math.min(bytes.length, perMaxLen);
        System.arraycopy(bytes, 0, result, 0, len);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T fromNative(Object nativeValue, FromNativeContext context) {
        byte[] buf = (byte[]) nativeValue;
        int len = 0;
        while (len < buf.length && buf[len] != 0) len++;
        try {
            T instance = (T) getClass().getDeclaredConstructor().newInstance();
            instance.value = new String(buf, 0, len, StandardCharsets.US_ASCII);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create " + getClass().getSimpleName(), e);
        }
    }

    // ==================== PER encode/decode ====================

    /** PER-encode with fixed length (no length prefix). */
    public byte[] encode() {
        byte[] bytes = getEncodableBytes();
        byte[] buf = new byte[4096];
        IntByReference outLen = new IntByReference(buf.length);
        perEncode(bytes, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    /** PER-decode with fixed length. */
    @SuppressWarnings("unchecked")
    public static <T extends CmsStringType<T>> T from(byte[] data, T instance) {
        byte[] val = new byte[instance.perMaxLen + 1];
        IntByReference cap = new IntByReference(instance.perMaxLen);
        instance.perDecode(data, val, cap);
        int len = 0;
        while (len < cap.getValue() && val[len] != 0) len++;
        instance.value = new String(val, 0, len, StandardCharsets.US_ASCII);
        return instance;
    }

    // Subclass hooks
    protected byte[] getEncodableBytes() {
        return value.getBytes(StandardCharsets.US_ASCII);
    }
    protected abstract void perEncode(byte[] bytes, byte[] buf, IntByReference outLen);
    protected abstract void perDecode(byte[] data, byte[] val, IntByReference cap);

    @Override
    public String toString() { return value; }
}
