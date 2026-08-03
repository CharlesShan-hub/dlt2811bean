package com.ysh.jcms.pdu.goose;

import com.ysh.jcms.data.InnerSetGoCBValuesResponsePDU;
import com.ysh.jcms.data.core.CmsSequence;

/**
 * SetGoCBValues-ResponsePDU ::= NULL — 8.9.5
 *
 * <p>
 * Response has no payload.
 */
public class CmsSetGoCbValuesResponse extends CmsSequence {

    public CmsSetGoCbValuesResponse() {
        super(new InnerSetGoCBValuesResponsePDU());
    }
}
