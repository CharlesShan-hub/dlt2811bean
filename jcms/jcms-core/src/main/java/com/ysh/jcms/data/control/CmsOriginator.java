package com.ysh.jcms.data.control;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
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

    public CmsOriginator() { super(Codec.ORIGINATOR);
        this.orCat   = new CmsOrCat();
        this.orIdent = new CmsUint8Array();
    }
    
    public CmsOriginator orCat(int v) { this.orCat.value(v); return this; }
    public CmsOriginator orIdent(byte[] v) { this.orIdent.value(v); return this; }
    public CmsOriginator orIdent(String v) { this.orIdent.value(v); return this; }
    
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(orCat, orIdent);
    }
}