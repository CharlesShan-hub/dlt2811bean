package com.ysh.jcms.data.common;

import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.InnerEntryID;
import com.ysh.jcms.data.string.CmsUint8Array;

/**
 * EntryID ::= OCTET STRING (SIZE(8)) — 7.3.8
 * <p>
 * Wraps {@link InnerEntryID} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsEntryId extends CmsUint8Array {
    public static final int LEN = 8;

    private transient InnerEntryID inner = new InnerEntryID();

    {
        this.codec = Codec.ENTRY_ID;
    }

    public CmsEntryId() {
        super(LEN);
    }
    public CmsEntryId(byte[] data) {
        super(LEN);
        inner.value = data;
    }

    @Override
    public byte[] value() {
        return inner.value;
    }
    @Override
    public CmsEntryId value(byte[] v) {
        inner.value = v;
        return this;
    }

    @Override
    public byte[] encode() {
        return inner.encode();
    }
    @Override
    public void decode(byte[] data) {
        inner = InnerEntryID.decode(data);
    }
}
