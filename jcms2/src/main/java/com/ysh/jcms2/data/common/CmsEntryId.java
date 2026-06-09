package com.ysh.jcms2.data.common;

import com.ysh.jcms2.data.string.CmsUint8Array;

/**
 * EntryID ::= OCTET STRING (SIZE(8))  —  7.3.8
 * Fixed 8-byte OCTET STRING.
 *
 * Use CmsUint8Array directly. This subclass is kept for
 * type-name documentation only.
 */
public class CmsEntryId extends CmsUint8Array {
    public CmsEntryId() {}
    public CmsEntryId(byte[] data) { super(data); }
}
