package com.ysh.jcms.data.common;

import com.sun.jna.Memory;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.string.CmsUint8Array;

/**
 * ObjectReference ::= VisibleString (SIZE(0..129))  —  7.3.2
 */
public class CmsObjectReference extends CmsUint8Array {
    public static final int MAX_LEN = 129;

    public CmsObjectReference() {}
    public CmsObjectReference(byte[] data) { super(data); }
    public CmsObjectReference(String s) { super(s); }

    @Override public byte[] encode() { write(); return NativeBridge.encodeObjectReference(nativePtr); }
    @Override public void decode(byte[] data) {
        this.ownedData = new Memory(MAX_LEN + 1);
        this.value = ownedData;
        this.len = MAX_LEN;
        write();
        NativeBridge.decodeObjectReference(nativePtr, data);
        read();
    }
}
