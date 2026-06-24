package com.ysh.jcms.data.common;

import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.string.CmsUint8Array;

/**
 * EntryID ::= OCTET STRING (SIZE(8))  —  7.3.8
 */
public class CmsEntryId extends CmsUint8Array {
    public CmsEntryId() { super(9, null); }
    public CmsEntryId(byte[] data) { super(data); }
}
