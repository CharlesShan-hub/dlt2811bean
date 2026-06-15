package com.ysh.jcms.data.common;

import com.sun.jna.Memory;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.string.CmsUint8Array;

/**
 * SubReference ::= VisibleString (SIZE(0..129))  —  7.3.3
 */
public class CmsSubReference extends CmsUint8Array {
    public static final int MAX_LEN = 129;

    public CmsSubReference() {}
    public CmsSubReference(byte[] data) { super(data); }
    public CmsSubReference(String s) { super(s); }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSubReference(nativePtr); }
    @Override public void decode(byte[] data) {
        this.ownedData = new Memory(MAX_LEN + 1);
        this.value = ownedData;
        this.len = MAX_LEN;
        write();
        NativeBridge.decodeSubReference(nativePtr, data);
        read();
    }
}
