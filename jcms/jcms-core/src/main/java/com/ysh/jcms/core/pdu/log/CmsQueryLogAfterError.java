package com.ysh.jcms.core.pdu.log;

import com.ysh.jcms.data.InnerQueryLogAfterErrorPDU;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * QueryLogAfter-ErrorPDU ::= ServiceError — 8.8.5
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
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
