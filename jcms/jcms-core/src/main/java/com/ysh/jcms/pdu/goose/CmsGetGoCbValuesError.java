package com.ysh.jcms.pdu.goose;

import com.ysh.jcms.data.InnerGetGoCbValuesErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * GetGoCbValues-ErrorPDU ::= ServiceError — 8.9.4
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsGetGoCbValuesError extends CmsServiceError {

    public CmsGetGoCbValuesError() {
        super(new InnerGetGoCbValuesErrorPDU());
    }

    public CmsGetGoCbValuesError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetGoCbValuesError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
