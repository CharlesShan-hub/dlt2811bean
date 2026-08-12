package com.ysh.jcms.core.pdu.log;

import com.ysh.jcms.data.InnerSetLCBValuesResponsePDU;
import com.ysh.jcms.core.data.core.CmsSequence;

/**
 * <pre>
 * {@code
 * SetLCBValues-ResponsePDU ::= NULL — 8.8.3
 * }
 * </pre>
 *
 * <p>
 * Response has no payload.
 */
public class CmsSetLcbValuesResponse extends CmsSequence {

    public CmsSetLcbValuesResponse() {
        super(new InnerSetLCBValuesResponsePDU());
    }
}
