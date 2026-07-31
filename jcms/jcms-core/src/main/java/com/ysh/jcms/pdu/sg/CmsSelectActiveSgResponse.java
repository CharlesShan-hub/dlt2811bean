package com.ysh.jcms.pdu.sg;

import com.ysh.jcms.data.InnerSelectActiveSGResponsePDU;
import com.ysh.jcms.data.core.CmsSequence;

/**
 * SelectActiveSG-ResponsePDU ::= NULL — 8.6.1
 *
 * <p>Response has no payload.
 */
public class CmsSelectActiveSgResponse extends CmsSequence {

    public CmsSelectActiveSgResponse() {
        super(new InnerSelectActiveSGResponsePDU());
    }
}
