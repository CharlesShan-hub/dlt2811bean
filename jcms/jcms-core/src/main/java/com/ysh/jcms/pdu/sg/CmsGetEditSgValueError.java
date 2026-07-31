package com.ysh.jcms.pdu.sg;

import com.ysh.jcms.data.InnerGetEditSGValueErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * GetEditSGValue-ErrorPDU ::= ServiceError — 8.6.5
 *
 * <p>Type alias, not a SEQUENCE.
 */
public class CmsGetEditSgValueError extends CmsServiceError {

    public CmsGetEditSgValueError() {
        super(new InnerGetEditSGValueErrorPDU());
    }

    public CmsGetEditSgValueError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetEditSgValueError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
