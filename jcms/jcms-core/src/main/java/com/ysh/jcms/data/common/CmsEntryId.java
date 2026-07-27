package com.ysh.jcms.data.common;

import com.ysh.jcms.core.CmsScalar;
import com.ysh.jcms.data.InnerEntryID;

/**
 * EntryID ::= OCTET STRING (SIZE(8)) — 7.3.8
 * <p>
 * Wraps {@link InnerEntryID} for PER encode/decode via Rust (libasn1.so).
 */
public class CmsEntryId extends CmsScalar {
    public static final int LEN = 8;

    public CmsEntryId() {
        super(new InnerEntryID());
    }
    public CmsEntryId(byte[] data) {
        this();
        innerSet(data);
    }

    public byte[] value() {
        return (byte[]) innerGet();
    }
    public CmsEntryId value(byte[] v) {
        if (v != null && v.length != LEN)
            throw new IllegalArgumentException("CmsEntryId must be exactly " + LEN + " bytes, got " + v.length);
        innerSet(v);
        return this;
    }
}
