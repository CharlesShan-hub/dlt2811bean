package com.ysh.jcms.pdu.report;

import com.ysh.jcms.data.InnerSetBRCBValuesResponsePDU;
import com.ysh.jcms.data.core.CmsSequence;

/**
 * SetBRCBValues-ResponsePDU ::= NULL — 8.7.3
 *
 * <p>Response has no payload.
 */
public class CmsSetBrcbValuesResponse extends CmsSequence {

    public CmsSetBrcbValuesResponse() {
        super(new InnerSetBRCBValuesResponsePDU());
    }
}
