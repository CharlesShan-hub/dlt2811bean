package com.ysh.jcms.pdu.goose;

import com.ysh.jcms.data.InnerGetGoReferenceErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * GetGoReference-ErrorPDU ::= ServiceError — 8.9.2
 *
 * <p>Type alias, not a SEQUENCE.
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
