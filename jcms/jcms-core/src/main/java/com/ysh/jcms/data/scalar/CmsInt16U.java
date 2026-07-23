package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.InnerInt16U;

/**
 * Wraps {@link InnerInt16U} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsInt16U extends CmsType {

    private transient InnerInt16U inner = new InnerInt16U();

    public CmsInt16U() {
        super(Codec.INT16U);
    }
    public CmsInt16U(int value) {
        super(Codec.INT16U);
        inner.value = value & 0xFFFF;
    }

    public int value() {
        return inner.value & 0xFFFF;
    }
    public CmsInt16U value(int v) {
        inner.value = v & 0xFFFF;
        return this;
    }

    @Override
    public byte[] encode() {
        return inner.encode();
    }
    @Override
    public void decode(byte[] data) {
        inner = InnerInt16U.decode(data);
    }

    @Override
    protected int calcNativeSize() {
        return 2;
    }
    @Override
    public void write() {
        nativePtr.setShort(0, (short) inner.value);
    }
    @Override
    public void read() {
        inner.value = nativePtr.getShort(0) & 0xFFFF;
    }
}
