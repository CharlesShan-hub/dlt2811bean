package com.ysh.jcms.pdu.log;

import com.ysh.jcms.data.InnerQueryLogAfterErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * QueryLogAfter-ErrorPDU ::= ServiceError — 8.8.5
 *
 * <p>Type alias, not a SEQUENCE.
 */
public class CmsQueryLogAfterError extends CmsServiceError {

    public CmsQueryLogAfterError() {
        super(new InnerQueryLogAfterErrorPDU());
    }

    public CmsQueryLogAfterError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsQueryLogAfterError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
