package com.ysh.jcms.data.common;

import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.string.CmsUint8Array;

/**
 * ObjectName ::= VisibleString (SIZE(0..64))  —  7.3.1
 */
public class CmsObjectName extends CmsUint8Array {
    public CmsObjectName() {}
    public CmsObjectName(byte[] data) { super(data); }
    public CmsObjectName(String s) { super(s); }

    @Override public byte[] encode() { write(); return NativeBridge.encodeObjectName(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeObjectName(nativePtr, data); read(); }
}
