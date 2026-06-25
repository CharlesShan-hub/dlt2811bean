package com.ysh.jcms.data.common;

import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.string.CmsUint8Array;

/**
 * SubReference ::= VisibleString (SIZE(0..129))  —  7.3.3
 */
public class CmsSubReference extends CmsUint8Array {
    public static final int MAX_LEN = 129;
    { this.codec = Codec.SUB_REFERENCE; }

    public CmsSubReference() {}
    public CmsSubReference(byte[] data) { super(data); }
    public CmsSubReference(String s) { super(s); }
}
