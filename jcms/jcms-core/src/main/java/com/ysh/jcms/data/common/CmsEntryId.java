package com.ysh.jcms.data.common;

import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.string.CmsUint8Array;

/**
 * EntryID ::= OCTET STRING (SIZE(8)) — 7.3.8
 */
public class CmsEntryId extends CmsUint8Array {
    public static final int LEN = 8;
    {
        this.codec = Codec.ENTRY_ID;
    }

    public CmsEntryId() {
        super(LEN);
    }
    public CmsEntryId(byte[] data) {
        super(data);
    }
}
