package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.InnerInt16;

/**
 * Wraps {@link InnerInt16} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsInt16 extends CmsType {

    private transient InnerInt16 inner = new InnerInt16();

    public CmsInt16() {
        super(Codec.INT16);
    }
    public CmsInt16(short value) {
        super(Codec.INT16);
        inner.value = value;
    }
    public CmsInt16(int value) {
        this((short) value);
    }

    public short value() {
        return (short) inner.value;
    }
    public CmsInt16 value(short v) {
        inner.value = v;
        return this;
    }
    public CmsInt16 value(int v) {
        return value((short) v);
    }

    @Override
    public byte[] encode() {
        return inner.encode();
    }
    @Override
    public void decode(byte[] data) {
        inner = InnerInt16.decode(data);
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
        inner.value = nativePtr.getShort(0);
    }
}
