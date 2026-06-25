package com.ysh.jcms.data.common;

import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.string.CmsUint8Array;

/**
 * ObjectReference ::= VisibleString (SIZE(0..129))  —  7.3.2
 */
public class CmsObjectReference extends CmsUint8Array {
    public static final int MAX_LEN = 129;
    { this.codec = Codec.OBJECT_REFERENCE; }

    public CmsObjectReference() {}
    public CmsObjectReference(byte[] data) { super(data); }
    public CmsObjectReference(String s) { super(s); }
}
