package com.ysh.jcms.core.pdu.goose;

import com.ysh.jcms.data.InnerGetGoReferenceErrorPDU;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * GetGoReference-ErrorPDU ::= ServiceError — 8.9.2
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsGetGoReferenceError extends CmsServiceError {

    public CmsGetGoReferenceError() {
        super(new InnerGetGoReferenceErrorPDU());
    }

    public CmsGetGoReferenceError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetGoReferenceError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
