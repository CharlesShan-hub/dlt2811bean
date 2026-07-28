package com.ysh.jcms.data.control;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerOriginator;

/**
 * Originator ::= SEQUENCE { orCat [0] INTEGER (0..8), orIdent [1] OCTET STRING (SIZE(0..64)) } — 7.5.2
 */
public class CmsOriginator extends CmsType {

    public final CmsOrCat orCat = new CmsOrCat();

    public CmsOriginator() {
        super(new InnerOriginator());
    }

    public CmsOriginator orIdent(byte[] v) {
        ((InnerOriginator) inner).orIdent.value = v;
        return this;
    }

    @Override
    public void syncToInner() {
        ((InnerOriginator) inner).orCat = orCat.value();
    }

    @Override
    public void syncFromInner() {
        orCat.value(((InnerOriginator) inner).orCat);
    }
}
