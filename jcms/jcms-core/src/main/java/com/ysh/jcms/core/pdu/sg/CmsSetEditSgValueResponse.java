package com.ysh.jcms.core.pdu.sg;

import com.ysh.jcms.data.InnerSetEditSGValueResponsePDU;
import com.ysh.jcms.core.data.core.CmsSequence;

/**
 * <pre>
 * {@code
 * SetEditSGValue-ResponsePDU ::= NULL — 8.6.3
 * }
 * </pre>
 *
 * <p>
 * Response has no payload.
 */
public class CmsSetEditSgValueResponse extends CmsSequence {

    public CmsSetEditSgValueResponse() {
        super(new InnerSetEditSGValueResponsePDU());
    }
}
