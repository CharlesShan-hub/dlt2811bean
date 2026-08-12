package com.ysh.jcms.core.pdu.file;

import com.ysh.jcms.data.InnerDeleteFileResponsePDU;
import com.ysh.jcms.core.data.core.CmsSequence;

/**
 * <pre>
 * {@code
 * DeleteFile-ResponsePDU ::= NULL — 8.12.3
 * }
 * </pre>
 *
 * <p>
 * Response has no payload.
 */
public class CmsDeleteFileResponse extends CmsSequence {

    public CmsDeleteFileResponse() {
        super(new InnerDeleteFileResponsePDU());
    }
}
