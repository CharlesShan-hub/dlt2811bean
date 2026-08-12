package com.ysh.jcms.core.pdu.sg;

import com.ysh.jcms.data.InnerSelectActiveSGResponsePDU;
import com.ysh.jcms.core.data.core.CmsSequence;

/**
 * <pre>
 * {@code
 * SelectActiveSG-ResponsePDU ::= NULL — 8.6.1
 * }
 * </pre>
 *
 * <p>
 * Response has no payload.
 */
public class CmsSelectActiveSgResponse extends CmsSequence {

    public CmsSelectActiveSgResponse() {
        super(new InnerSelectActiveSGResponsePDU());
    }
}
