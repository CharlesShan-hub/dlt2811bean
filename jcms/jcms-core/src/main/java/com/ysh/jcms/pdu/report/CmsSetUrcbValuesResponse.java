package com.ysh.jcms.pdu.report;

import com.ysh.jcms.data.InnerSetURCBValuesResponsePDU;
import com.ysh.jcms.data.core.CmsSequence;

/**
 * SetURCBValues-ResponsePDU ::= NULL — 8.7.5
 *
 * <p>
 * Response has no payload.
 */
public class CmsSetUrcbValuesResponse extends CmsSequence {

    public CmsSetUrcbValuesResponse() {
        super(new InnerSetURCBValuesResponsePDU());
    }
}
