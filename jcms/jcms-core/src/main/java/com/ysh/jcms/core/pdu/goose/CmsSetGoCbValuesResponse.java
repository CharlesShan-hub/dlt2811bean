package com.ysh.jcms.core.pdu.goose;

import com.ysh.jcms.data.InnerSetGoCBValuesResponsePDU;
import com.ysh.jcms.core.data.core.CmsSequence;

/**
 * <pre>
 * {@code
 * SetGoCBValues-ResponsePDU ::= NULL — 8.9.5
 * }
 * </pre>
 *
 * <p>
 * Response has no payload.
 */
public class CmsSetGoCbValuesResponse extends CmsSequence {

    public CmsSetGoCbValuesResponse() {
        super(new InnerSetGoCBValuesResponsePDU());
    }
}
