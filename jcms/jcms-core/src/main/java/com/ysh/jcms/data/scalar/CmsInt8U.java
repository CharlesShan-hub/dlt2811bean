package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.InnerInt8U;

/**
 * Wraps {@link InnerInt8U} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsInt8U extends CmsType {

    private transient InnerInt8U inner = new InnerInt8U();

    public CmsInt8U() {
        super(Codec.INT8U);
    }
    public CmsInt8U(int value) {
        super(Codec.INT8U);
        inner.value = value & 0xFF;
    }

    public int value() {
        return inner.value & 0xFF;
    }
    public CmsInt8U value(int v) {
        inner.value = v & 0xFF;
        return this;
    }

    @Override
    public byte[] encode() {
        return inner.encode();
    }
    @Override
    public void decode(byte[] data) {
        inner = InnerInt8U.decode(data);
    }

    @Override
    protected int calcNativeSize() {
        return 1;
    }
    @Override
    public void write() {
        nativePtr.setByte(0, (byte) inner.value);
    }
    @Override
    public void read() {
        inner.value = nativePtr.getByte(0) & 0xFF;
    }
}
