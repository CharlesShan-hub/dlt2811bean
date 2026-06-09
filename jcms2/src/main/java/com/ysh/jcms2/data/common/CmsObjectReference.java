package com.ysh.jcms2.data.common;

import com.ysh.jcms2.data.string.CmsUint8Array;

/**
 * ObjectReference ::= VisibleString (SIZE(0..129))  —  7.3.2
 *
 * Use CmsUint8Array directly. This subclass is kept for
 * type-name documentation only.
 */
public class CmsObjectReference extends CmsUint8Array {
    public CmsObjectReference() {}
    public CmsObjectReference(byte[] data) { super(data); }
    public CmsObjectReference(String s) { super(s); }
}
