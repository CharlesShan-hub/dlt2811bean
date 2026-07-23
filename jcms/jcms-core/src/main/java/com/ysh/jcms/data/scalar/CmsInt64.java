package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.InnerInt64;

/**
 * Wraps {@link InnerInt64} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsInt64 extends CmsType {

    private transient InnerInt64 inner = new InnerInt64();

    public CmsInt64() {
        super(Codec.INT64);
    }
    public CmsInt64(long value) {
        super(Codec.INT64);
        inner.value = value;
    }

    public long value() {
        return inner.value;
    }
    public CmsInt64 value(long v) {
        inner.value = v;
        return this;
    }

    @Override
    public byte[] encode() {
        return inner.encode();
    }
    @Override
    public void decode(byte[] data) {
        inner = InnerInt64.decode(data);
    }

    @Override
    protected int calcNativeSize() {
        return 8;
    }
    @Override
    public void write() {
        nativePtr.setLong(0, inner.value);
    }
    @Override
    public void read() {
        inner.value = nativePtr.getLong(0);
    }
}
