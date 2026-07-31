package com.ysh.jcms.pdu.sg;

import com.ysh.jcms.data.InnerSelectEditSGResponsePDU;
import com.ysh.jcms.data.core.CmsSequence;

/**
 * SelectEditSG-ResponsePDU ::= NULL — 8.6.2
 *
 * <p>Response has no payload.
 */
public class CmsSelectEditSgResponse extends CmsSequence {

    public CmsSelectEditSgResponse() {
        super(new InnerSelectEditSGResponsePDU());
    }
}
