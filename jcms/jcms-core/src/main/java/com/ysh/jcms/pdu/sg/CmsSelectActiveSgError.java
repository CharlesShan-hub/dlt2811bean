package com.ysh.jcms.pdu.sg;

import com.ysh.jcms.data.InnerSelectActiveSGErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * SelectActiveSG-ErrorPDU ::= ServiceError — 8.6.1
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsSelectActiveSgError extends CmsServiceError {

    public CmsSelectActiveSgError() {
        super(new InnerSelectActiveSGErrorPDU());
    }

    public CmsSelectActiveSgError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsSelectActiveSgError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
