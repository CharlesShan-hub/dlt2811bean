package com.ysh.jcms.datatypes2.ffi;

import com.sun.jna.IntegerType;
import com.sun.jna.ptr.IntByReference;

/**
 * 所有 IntegerType 映射类型的基类。
 *
 * <p>子类只需声明 {@code SIZE} 并提供无参/有参构造，即可直接作为
 * {@link Structure} 字段被 JNA 识别。
 *
 * @param <T> 子类自身类型（self-type 用于链式调用）
 */
public abstract class CmsIntegerType<T extends CmsIntegerType<T>> extends IntegerType {

    protected CmsIntegerType(int size, long value, boolean unsigned) {
        super(size, value, unsigned);
    }

    /** 编码当前值为字节数组。 */
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        ffiEncode(buf, outLen);
        return java.util.Arrays.copyOf(buf, outLen.getValue());
    }

    /** C FFI 编码。 */
    protected abstract int ffiEncode(byte[] buf, IntByReference outLen);

    /**
     * 从字节数组解码，覆盖当前值。
     * 子类有独立解码能力时可重写，或直接使用静态 {@code from()}。
     */
    @SuppressWarnings("unchecked")
    public T decode(byte[] data) {
        IntByReference val = new IntByReference();
        ffiDecode(data, val);
        setValue(val.getValue());
        return (T) this;
    }

    /**
     * C FFI 解码 — 将 decoded 值写入 {@code value}。
     * 子类可重写（默认抛异常，表示不支持独立解码）。
     */
    protected void ffiDecode(byte[] data, IntByReference value) {
        throw new UnsupportedOperationException(
            getClass().getSimpleName() + " has no standalone FFI decode");
    }
}
