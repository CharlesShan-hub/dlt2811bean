package com.ysh.jcms.data.fc;

import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.string.CmsUint8Array;

/**
 * FunctionalConstraint ::= VisibleString (SIZE(2))  —  7.4
 * Fixed-size VisibleString, 2 bytes.
 */
public class CmsFunctionalConstraint extends CmsUint8Array {

    @Override public byte[] encode() { write(); return NativeBridge.encodeFunctionalConstraint(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeFunctionalConstraint(nativePtr, data); read(); }
}
