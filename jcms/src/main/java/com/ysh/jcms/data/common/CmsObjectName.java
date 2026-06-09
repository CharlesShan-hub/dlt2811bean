package com.ysh.jcms.data.common;

import com.ysh.jcms.data.string.CmsUint8Array;

/**
 * ObjectName ::= VisibleString (SIZE(0..64))  —  7.3.1
 *
 * Use CmsUint8Array directly. This subclass is kept for
 * type-name documentation only.
 */
public class CmsObjectName extends CmsUint8Array {
    public CmsObjectName() {}
    public CmsObjectName(byte[] data) { super(data); }
    public CmsObjectName(String s) { super(s); }
}
