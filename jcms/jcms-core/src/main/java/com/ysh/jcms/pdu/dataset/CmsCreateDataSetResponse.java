package com.ysh.jcms.pdu.dataset;

import com.ysh.jcms.data.InnerCreateDataSetResponsePDU;
import com.ysh.jcms.data.core.CmsSequence;

/**
 * CreateDataSet-ResponsePDU ::= NULL — 8.5.3
 *
 * <p>Response has no payload.
 */
public class CmsCreateDataSetResponse extends CmsSequence {

    public CmsCreateDataSetResponse() {
        super(new InnerCreateDataSetResponsePDU());
    }
}
