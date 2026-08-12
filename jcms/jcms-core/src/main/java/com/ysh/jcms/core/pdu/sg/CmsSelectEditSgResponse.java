package com.ysh.jcms.core.pdu.sg;

import com.ysh.jcms.data.InnerSelectEditSGResponsePDU;
import com.ysh.jcms.core.data.core.CmsSequence;

/**
 * <pre>
 * {@code
 * SelectEditSG-ResponsePDU ::= NULL — 8.6.2
 * }
 * </pre>
 *
 * <p>
 * Response has no payload.
 */
public class CmsSelectEditSgResponse extends CmsSequence {

    public CmsSelectEditSgResponse() {
        super(new InnerSelectEditSGResponsePDU());
    }
}
