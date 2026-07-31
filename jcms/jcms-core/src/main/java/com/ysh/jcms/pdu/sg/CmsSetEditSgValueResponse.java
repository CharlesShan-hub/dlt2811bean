package com.ysh.jcms.pdu.sg;

import com.ysh.jcms.data.InnerSetEditSGValueResponsePDU;
import com.ysh.jcms.data.core.CmsSequence;

/**
 * SetEditSGValue-ResponsePDU ::= NULL — 8.6.3
 *
 * <p>Response has no payload.
 */
public class CmsSetEditSgValueResponse extends CmsSequence {

    public CmsSetEditSgValueResponse() {
        super(new InnerSetEditSGValueResponsePDU());
    }
}
