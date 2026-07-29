package com.ysh.jcms.data.sequence.common;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerOriginator;
import com.ysh.jcms.data.enumerate.CmsOrCat;
import com.ysh.jcms.data.core.CmsOctetString;

/**
 * Originator ::= SEQUENCE { orCat [0] INTEGER (0..8), orIdent [1] OCTET STRING (SIZE(0..64)) } — 7.5.2
 */
public class CmsOriginator extends CmsSequence {

    @CmsField public CmsOrCat orCat;
    @CmsField public CmsOctetString orIdent;

    public CmsOriginator() {
        super(new InnerOriginator());
    }

    public CmsOriginator orCat(int v) { this.orCat.value(v); return this; }
    public CmsOriginator orIdent(byte[] v) { this.orIdent.value(v); return this; }

    /** Copy all field values from another CmsOriginator (fluent). */
    public CmsOriginator value(CmsOriginator v) {
        return orCat(v.orCat.value())
            .orIdent(v.orIdent.value());
    }
}
