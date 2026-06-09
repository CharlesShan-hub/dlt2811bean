package com.ysh.jcms2.data.common;

import com.ysh.jcms2.data.string.CmsUint8Array;

/**
 * SubReference ::= VisibleString (SIZE(0..129))  —  7.3.3
 *
 * Use CmsUint8Array directly. This subclass is kept for
 * type-name documentation only.
 */
public class CmsSubReference extends CmsUint8Array {
    public CmsSubReference() {}
    public CmsSubReference(byte[] data) { super(data); }
    public CmsSubReference(String s) { super(s); }
}
