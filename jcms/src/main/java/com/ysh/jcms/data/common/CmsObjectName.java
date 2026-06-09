package com.ysh.jcms.data.common;

import com.sun.jna.Memory;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.string.CmsUint8Array;

/**
 * ObjectName ::= VisibleString (SIZE(0..64))  —  7.3.1
 */
public class CmsObjectName extends CmsUint8Array {
    public static final int MAX_LEN = 64;

    public CmsObjectName() {}
    public CmsObjectName(byte[] data) { super(data); }
    public CmsObjectName(String s) { super(s); }

    @Override public byte[] encode() { write(); return NativeBridge.encodeObjectName(nativePtr); }
    @Override public void decode(byte[] data) {
        this.ownedData = new Memory(MAX_LEN + 1);
        this.value = ownedData;
        this.len = MAX_LEN;
        write();
        NativeBridge.decodeObjectName(nativePtr, data);
        read();
    }
}
