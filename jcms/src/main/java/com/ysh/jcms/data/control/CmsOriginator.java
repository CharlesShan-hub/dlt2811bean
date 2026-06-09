package com.ysh.jcms.data.control;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.string.CmsUint8Array;
import java.util.Arrays;
import java.util.List;

/**
 * Originator ::= SEQUENCE {
 *     orCat   [0] INTEGER (0..8) → CmsOrCat,
 *     orIdent [1] OCTET STRING (SIZE(0..64))
 * }  —  7.5.2
 *
 * All-pointer container:
 *   [0] orCat   → CmsOrCat*
 *   [8] orIdent → CmsUint8Array*
 */
public class CmsOriginator extends CmsType {

    public CmsOrCat      orCat;
    public CmsUint8Array orIdent;

    public CmsOriginator() {
        this.orCat   = new CmsOrCat();
        this.orIdent = new CmsUint8Array();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(orCat, orIdent);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeOriginator(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeOriginator(nativePtr, data); read(); }
}
