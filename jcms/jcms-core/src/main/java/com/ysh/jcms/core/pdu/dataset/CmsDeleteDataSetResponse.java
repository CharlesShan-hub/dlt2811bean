package com.ysh.jcms.core.pdu.dataset;

import com.ysh.jcms.data.InnerDeleteDataSetResponsePDU;
import com.ysh.jcms.core.data.core.CmsSequence;

/**
 * <pre>
 * {@code
 * DeleteDataSet-ResponsePDU ::= NULL — 8.5.4
 * }
 * </pre>
 *
 * <p>
 * Response has no payload.
 */
public class CmsDeleteDataSetResponse extends CmsSequence {

    public CmsDeleteDataSetResponse() {
        super(new InnerDeleteDataSetResponsePDU());
    }
}
