package com.ysh.jcms.pdu.file;

import com.ysh.jcms.data.InnerDeleteFileResponsePDU;
import com.ysh.jcms.data.core.CmsSequence;

/**
 * DeleteFile-ResponsePDU ::= NULL — 8.12.3
 *
 * <p>Response has no payload.
 */
public class CmsDeleteFileResponse extends CmsSequence {

    public CmsDeleteFileResponse() {
        super(new InnerDeleteFileResponsePDU());
    }
}
