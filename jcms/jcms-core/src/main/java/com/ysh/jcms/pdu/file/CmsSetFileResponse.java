package com.ysh.jcms.pdu.file;

import com.ysh.jcms.data.InnerSetFileResponsePDU;
import com.ysh.jcms.data.core.CmsSequence;

/**
 * SetFile-ResponsePDU ::= NULL — 8.12.2
 *
 * <p>Response has no payload.
 */
public class CmsSetFileResponse extends CmsSequence {

    public CmsSetFileResponse() {
        super(new InnerSetFileResponsePDU());
    }
}
