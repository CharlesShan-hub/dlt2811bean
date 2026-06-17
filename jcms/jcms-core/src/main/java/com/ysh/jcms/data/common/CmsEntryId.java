package com.ysh.jcms.data.common;

import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.string.CmsUint8Array;

/**
 * EntryID ::= OCTET STRING (SIZE(8))  —  7.3.8
 */
public class CmsEntryId extends CmsUint8Array {
    public CmsEntryId() { super(9, null); }
    public CmsEntryId(byte[] data) { super(data); }

    @Override public byte[] encode() { write(); return NativeBridge.encodeEntryId(nativePtr); }
    @Override public void decode(byte[] data) {
        this.len = 8;
        write();
        NativeBridge.decodeEntryId(nativePtr, data);
        read();
    }
}
