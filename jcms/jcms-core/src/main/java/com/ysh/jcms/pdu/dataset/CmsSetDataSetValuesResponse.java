package com.ysh.jcms.pdu.dataset;

import com.ysh.jcms.data.InnerSetDataSetValuesResponsePDU;
import com.ysh.jcms.data.core.CmsSequence;

/**
 * <pre>
 * {@code
 * SetDataSetValues-ResponsePDU ::= NULL — 8.5.2
 * }
 * </pre>
 *
 * <p>
 * Response has no payload.
 */
public class CmsSetDataSetValuesResponse extends CmsSequence {

    public CmsSetDataSetValuesResponse() {
        super(new InnerSetDataSetValuesResponsePDU());
    }
}
