package com.ysh.jcms.core.pdu.data;

import com.ysh.jcms.data.InnerSetDataValuesResponsePDU;
import com.ysh.jcms.core.data.core.CmsSequence;

/**
 * <pre>
 * {@code
 * SetDataValues-ResponsePDU ::= NULL — 8.4.2
 * }
 * </pre>
 *
 * <p>
 * Response has no payload.
 */
public class CmsSetDataValuesResponse extends CmsSequence {

    public CmsSetDataValuesResponse() {
        super(new InnerSetDataValuesResponsePDU());
    }
}
