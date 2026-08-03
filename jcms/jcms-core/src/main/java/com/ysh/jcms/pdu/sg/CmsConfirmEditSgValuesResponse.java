package com.ysh.jcms.pdu.sg;

import com.ysh.jcms.data.InnerConfirmEditSGValuesResponsePDU;
import com.ysh.jcms.data.core.CmsSequence;

/**
 * ConfirmEditSGValues-ResponsePDU ::= NULL — 8.6.4
 *
 * <p>
 * Response has no payload.
 */
public class CmsConfirmEditSgValuesResponse extends CmsSequence {

    public CmsConfirmEditSgValuesResponse() {
        super(new InnerConfirmEditSGValuesResponsePDU());
    }
}
