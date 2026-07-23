package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.InnerInt32U;
import com.ysh.jcms.data.InnerNative;

/**
 * Wraps {@link InnerInt32U} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsInt32U extends CmsType {

    private transient InnerInt32U inner = new InnerInt32U();

    public CmsInt32U() {
        super(Codec.INT32U);
    }
    public CmsInt32U(long value) {
        super(Codec.INT32U);
        inner.value = (int) (value & 0xFFFFFFFFL);
    }

    public long value() {
        return inner.value & 0xFFFFFFFFL;
    }
    public CmsInt32U value(long v) {
        inner.value = (int) (v & 0xFFFFFFFFL);
        return this;
    }

    @Override
    public byte[] encode() {
        // Send unsigned long value; inner.value is signed int
        long unsigned = inner.value & 0xFFFFFFFFL;
        return InnerNative.encode("Int32U", "aper", String.valueOf(unsigned));
    }
    @Override
    public void decode(byte[] data) {
        inner = InnerInt32U.decode(data);
    }

    @Override
    protected int calcNativeSize() {
        return 4;
    }
    @Override
    public void write() {
        nativePtr.setInt(0, (int) inner.value);
    }
    @Override
    public void read() {
        inner.value = nativePtr.getInt(0);
    }
}
