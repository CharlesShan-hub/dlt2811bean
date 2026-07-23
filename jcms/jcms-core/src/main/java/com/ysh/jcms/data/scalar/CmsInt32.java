package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.InnerInt32;

/**
 * Wraps {@link InnerInt32} for PER encode/decode via Rust (libasn1.so).
 * <p>
 * Retains {@link CmsType} compatibility ({@link #write()}/{@link #read()}/{@link #nativePtr})
 * so that container types can still use {@code children()} traversal.
 */
public class CmsInt32 extends CmsType {

    private transient InnerInt32 inner = new InnerInt32();

    public CmsInt32() {
        super(Codec.INT32);
    }
    public CmsInt32(int value) {
        super(Codec.INT32);
        inner.value = value;
    }

    public int value() {
        return inner.value;
    }
    public CmsInt32 value(int v) {
        inner.value = v;
        return this;
    }

    @Override
    public byte[] encode() {
        return inner.encode();
    }
    @Override
    public void decode(byte[] data) {
        inner = InnerInt32.decode(data);
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
