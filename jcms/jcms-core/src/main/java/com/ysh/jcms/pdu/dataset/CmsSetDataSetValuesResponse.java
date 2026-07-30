package com.ysh.jcms.pdu.dataset;

import com.ysh.jcms.data.InnerSetDataSetValuesResponsePDU;
import com.ysh.jcms.data.core.CmsSequence;

/**
 * SetDataSetValues-ResponsePDU ::= NULL — 8.5.2
 *
 * <p>Response has no payload.
 */
public class CmsSetDataSetValuesResponse extends CmsSequence {

    public CmsSetDataSetValuesResponse() {
        super(new InnerSetDataSetValuesResponsePDU());
    }
}
