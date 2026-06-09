package com.ysh.jcms.data.common;

import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.string.CmsUint8Array;

/**
 * SubReference ::= VisibleString (SIZE(0..129))  —  7.3.3
 */
public class CmsSubReference extends CmsUint8Array {
    public CmsSubReference() {}
    public CmsSubReference(byte[] data) { super(data); }
    public CmsSubReference(String s) { super(s); }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSubReference(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSubReference(nativePtr, data); read(); }
}
