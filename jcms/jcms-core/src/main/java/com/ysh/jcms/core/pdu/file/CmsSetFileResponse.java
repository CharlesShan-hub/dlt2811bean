package com.ysh.jcms.core.pdu.file;

import com.ysh.jcms.data.InnerSetFileResponsePDU;
import com.ysh.jcms.core.data.core.CmsSequence;

/**
 * <pre>
 * {@code
 * SetFile-ResponsePDU ::= NULL — 8.12.2
 * }
 * </pre>
 *
 * <p>
 * Response has no payload.
 */
public class CmsSetFileResponse extends CmsSequence {

    public CmsSetFileResponse() {
        super(new InnerSetFileResponsePDU());
    }
}
