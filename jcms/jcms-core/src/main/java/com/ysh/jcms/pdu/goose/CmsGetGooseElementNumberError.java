package com.ysh.jcms.pdu.goose;

import com.ysh.jcms.data.InnerGetGOOSEElementNumberErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * GetGOOSEElementNumber-ErrorPDU ::= ServiceError — 8.9.3
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsGetGooseElementNumberError extends CmsServiceError {

    public CmsGetGooseElementNumberError() {
        super(new InnerGetGOOSEElementNumberErrorPDU());
    }

    public CmsGetGooseElementNumberError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetGooseElementNumberError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
