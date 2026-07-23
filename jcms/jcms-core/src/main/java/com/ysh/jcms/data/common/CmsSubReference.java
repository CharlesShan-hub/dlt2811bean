package com.ysh.jcms.data.common;

import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.InnerSubReference;
import com.ysh.jcms.data.string.CmsUint8Array;
import java.nio.charset.StandardCharsets;

/**
 * SubReference ::= VisibleString (SIZE(0..129)) — 7.3.3
 * <p>
 * Wraps {@link InnerSubReference} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsSubReference extends CmsUint8Array {
    public static final int MAX_LEN = 129;

    private transient InnerSubReference inner = new InnerSubReference();

    {
        this.codec = Codec.SUB_REFERENCE;
    }

    public CmsSubReference() {
    }
    public CmsSubReference(byte[] data) {
        inner.value = new String(data, StandardCharsets.UTF_8);
    }
    public CmsSubReference(String s) {
        inner.value = s;
    }

    @Override
    public byte[] value() {
        return inner.value.getBytes(StandardCharsets.UTF_8);
    }
    @Override
    public CmsSubReference value(byte[] v) {
        inner.value = new String(v, StandardCharsets.UTF_8);
        return this;
    }
    @Override
    public CmsSubReference value(String s) {
        inner.value = s;
        return this;
    }

    @Override
    public byte[] encode() {
        return inner.encode();
    }
    @Override
    public void decode(byte[] data) {
        inner = InnerSubReference.decode(data);
    }
}
