package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.InnerSetDataValuesResponsePDU;
import com.ysh.jcms.data.core.CmsSequence;

/**
 * SetDataValues-ResponsePDU ::= NULL — 8.4.2
 *
 * <p>Response has no payload.
 */
public class CmsSetDataValuesResponse extends CmsSequence {

    public CmsSetDataValuesResponse() {
        super(new InnerSetDataValuesResponsePDU());
    }
}
