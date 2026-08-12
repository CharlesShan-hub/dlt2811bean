package com.ysh.jcms.core.pdu.msv;

import com.ysh.jcms.data.InnerSetMSVCBValuesResponsePDU;
import com.ysh.jcms.core.data.core.CmsSequence;

/**
 * <pre>
 * {@code
 * SetMSVCBValues-ResponsePDU ::= NULL — 8.10.3
 * }
 * </pre>
 *
 * <p>
 * Response has no payload.
 */
public class CmsSetMsvcbValuesResponse extends CmsSequence {

    public CmsSetMsvcbValuesResponse() {
        super(new InnerSetMSVCBValuesResponsePDU());
    }
}
