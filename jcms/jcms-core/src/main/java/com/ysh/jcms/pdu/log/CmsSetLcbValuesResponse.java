package com.ysh.jcms.pdu.log;

import com.ysh.jcms.data.InnerSetLCBValuesResponsePDU;
import com.ysh.jcms.data.core.CmsSequence;

/**
 * SetLCBValues-ResponsePDU ::= NULL — 8.8.3
 *
 * <p>
 * Response has no payload.
 */
public class CmsSetLcbValuesResponse extends CmsSequence {

    public CmsSetLcbValuesResponse() {
        super(new InnerSetLCBValuesResponsePDU());
    }
}
