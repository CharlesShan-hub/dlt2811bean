package com.ysh.jcms.data.common;

import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.string.CmsUint8Array;

/**
 * ObjectName ::= VisibleString (SIZE(0..64)) — 7.3.1
 */
public class CmsObjectName extends CmsUint8Array {
    public static final int MAX_LEN = 64;
    {
        this.codec = Codec.OBJECT_NAME;
    }

    public CmsObjectName() {
    }
    public CmsObjectName(byte[] data) {
        super(data);
    }
    public CmsObjectName(String s) {
        super(s);
    }
}
