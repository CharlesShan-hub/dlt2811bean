package com.ysh.jcms.pdu.dataset;

import com.ysh.jcms.data.InnerCreateDataSetResponsePDU;
import com.ysh.jcms.data.core.CmsSequence;

/**
 * <pre>
 * {@code
 * CreateDataSet-ResponsePDU ::= NULL — 8.5.3
 * }
 * </pre>
 *
 * <p>
 * Response has no payload.
 */
public class CmsCreateDataSetResponse extends CmsSequence {

    public CmsCreateDataSetResponse() {
        super(new InnerCreateDataSetResponsePDU());
    }
}
