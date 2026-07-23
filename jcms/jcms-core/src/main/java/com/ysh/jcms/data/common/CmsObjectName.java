package com.ysh.jcms.data.common;

import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.InnerObjectName;
import com.ysh.jcms.data.string.CmsUint8Array;
import java.nio.charset.StandardCharsets;

/**
 * ObjectName ::= VisibleString (SIZE(0..64)) — 7.3.1
 * <p>
 * Wraps {@link InnerObjectName} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsObjectName extends CmsUint8Array {
    public static final int MAX_LEN = 64;

    private transient InnerObjectName inner = new InnerObjectName();

    {
        this.codec = Codec.OBJECT_NAME;
    }

    public CmsObjectName() {
    }
    public CmsObjectName(byte[] data) {
        inner.value = new String(data, StandardCharsets.UTF_8);
    }
    public CmsObjectName(String s) {
        inner.value = s;
    }

    @Override
    public byte[] value() {
        return inner.value.getBytes(StandardCharsets.UTF_8);
    }
    @Override
    public CmsObjectName value(byte[] v) {
        inner.value = new String(v, StandardCharsets.UTF_8);
        return this;
    }
    @Override
    public CmsObjectName value(String s) {
        inner.value = s;
        return this;
    }

    @Override
    public byte[] encode() {
        return inner.encode();
    }
    @Override
    public void decode(byte[] data) {
        inner = InnerObjectName.decode(data);
    }
}
