package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.InnerBoolean;

/**
 * Wraps {@link InnerBoolean} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsBoolean extends CmsType {

    private transient InnerBoolean inner = new InnerBoolean();

    public CmsBoolean() {
        super(Codec.BOOLEAN);
    }
    public CmsBoolean(boolean value) {
        super(Codec.BOOLEAN);
        inner.value = value ? 1 : 0;
    }

    public boolean value() {
        return inner.value != 0;
    }
    public CmsBoolean value(boolean v) {
        inner.value = v ? 1 : 0;
        return this;
    }

    @Override
    public byte[] encode() {
        return inner.encode();
    }
    @Override
    public void decode(byte[] data) {
        inner = InnerBoolean.decode(data);
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
