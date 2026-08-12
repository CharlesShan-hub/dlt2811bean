package com.ysh.jcms.core.pdu.report;

import com.ysh.jcms.data.InnerSetBRCBValuesResponsePDU;
import com.ysh.jcms.core.data.core.CmsSequence;

/**
 * <pre>
 * {@code
 * SetBRCBValues-ResponsePDU ::= NULL — 8.7.3
 * }
 * </pre>
 *
 * <p>
 * Response has no payload.
 */
public class CmsSetBrcbValuesResponse extends CmsSequence {

    public CmsSetBrcbValuesResponse() {
        super(new InnerSetBRCBValuesResponsePDU());
    }
}
