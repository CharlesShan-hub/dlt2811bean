package com.ysh.jcms.pdu.dataset;

import com.ysh.jcms.data.InnerDeleteDataSetResponsePDU;
import com.ysh.jcms.data.core.CmsSequence;

/**
 * DeleteDataSet-ResponsePDU ::= NULL — 8.5.4
 *
 * <p>
 * Response has no payload.
 */
public class CmsDeleteDataSetResponse extends CmsSequence {

    public CmsDeleteDataSetResponse() {
        super(new InnerDeleteDataSetResponsePDU());
    }
}
