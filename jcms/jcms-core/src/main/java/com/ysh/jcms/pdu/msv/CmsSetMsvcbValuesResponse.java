package com.ysh.jcms.pdu.msv;

import com.ysh.jcms.data.InnerSetMSVCBValuesResponsePDU;
import com.ysh.jcms.data.core.CmsSequence;

/**
 * SetMSVCBValues-ResponsePDU ::= NULL — 8.10.3
 *
 * <p>
 * Response has no payload.
 */
public class CmsSetMsvcbValuesResponse extends CmsSequence {

    public CmsSetMsvcbValuesResponse() {
        super(new InnerSetMSVCBValuesResponsePDU());
    }
}
