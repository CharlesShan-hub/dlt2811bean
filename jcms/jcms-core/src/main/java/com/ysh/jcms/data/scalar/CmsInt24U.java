package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.InnerInt24U;

/**
 * Wraps {@link InnerInt24U} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsInt24U extends CmsType {

    public static final int MAX = 16777215;

    private transient InnerInt24U inner = new InnerInt24U();

    public CmsInt24U() {
        super(Codec.INT24U);
    }
    public CmsInt24U(int value) {
        super(Codec.INT24U);
        inner.value = value & MAX;
    }

    public int value() {
        return inner.value;
    }
    public CmsInt24U value(int v) {
        inner.value = v & MAX;
        return this;
    }

    @Override
    public byte[] encode() {
        return inner.encode();
    }
    @Override
    public void decode(byte[] data) {
        inner = InnerInt24U.decode(data);
    }

    @Override
    protected int calcNativeSize() {
        return 4;
    }
    @Override
    public void write() {
        nativePtr.setInt(0, inner.value);
    }
    @Override
    public void read() {
        inner.value = nativePtr.getInt(0);
    }
}
