package com.ysh.jcms.core.pdu.sg;

import com.ysh.jcms.data.InnerConfirmEditSGValuesResponsePDU;
import com.ysh.jcms.core.data.core.CmsSequence;

/**
 * <pre>
 * {@code
 * ConfirmEditSGValues-ResponsePDU ::= NULL — 8.6.4
 * }
 * </pre>
 *
 * <p>
 * Response has no payload.
 */
public class CmsConfirmEditSgValuesResponse extends CmsSequence {

    public CmsConfirmEditSgValuesResponse() {
        super(new InnerConfirmEditSGValuesResponsePDU());
    }
}
