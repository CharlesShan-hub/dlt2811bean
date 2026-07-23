package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.InnerInt8;

/**
 * Wraps {@link InnerInt8} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsInt8 extends CmsType {

    private transient InnerInt8 inner = new InnerInt8();

    public CmsInt8() {
        super(Codec.INT8);
    }
    public CmsInt8(byte value) {
        super(Codec.INT8);
        inner.value = value;
    }
    public CmsInt8(int value) {
        this((byte) value);
    }

    public byte value() {
        return (byte) inner.value;
    }
    public CmsInt8 value(byte v) {
        inner.value = v;
        return this;
    }
    public CmsInt8 value(int v) {
        return value((byte) v);
    }

    @Override
    public byte[] encode() {
        return inner.encode();
    }
    @Override
    public void decode(byte[] data) {
        inner = InnerInt8.decode(data);
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
        inner.value = nativePtr.getByte(0);
    }
}
