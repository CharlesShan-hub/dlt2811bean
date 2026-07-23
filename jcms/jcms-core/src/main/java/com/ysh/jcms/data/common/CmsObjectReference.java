package com.ysh.jcms.data.common;

import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.InnerObjectReference;
import com.ysh.jcms.data.string.CmsUint8Array;
import java.nio.charset.StandardCharsets;

/**
 * ObjectReference ::= VisibleString (SIZE(0..129)) — 7.3.2
 * <p>
 * Wraps {@link InnerObjectReference} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsObjectReference extends CmsUint8Array {
    public static final int MAX_LEN = 129;

    private transient InnerObjectReference inner = new InnerObjectReference();

    {
        this.codec = Codec.OBJECT_REFERENCE;
    }

    public CmsObjectReference() {
    }
    public CmsObjectReference(byte[] data) {
        inner.value = new String(data, StandardCharsets.UTF_8);
    }
    public CmsObjectReference(String s) {
        inner.value = s;
    }

    @Override
    public byte[] value() {
        return inner.value.getBytes(StandardCharsets.UTF_8);
    }
    @Override
    public CmsObjectReference value(byte[] v) {
        inner.value = new String(v, StandardCharsets.UTF_8);
        return this;
    }
    @Override
    public CmsObjectReference value(String s) {
        inner.value = s;
        return this;
    }

    @Override
    public byte[] encode() {
        return inner.encode();
    }
    @Override
    public void decode(byte[] data) {
        inner = InnerObjectReference.decode(data);
    }
}
