package com.ysh.jcms.pdu.report;

import com.ysh.jcms.data.InnerGetURCBValuesErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * GetURCBValues-ErrorPDU ::= ServiceError — 8.7.4
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsGetUrcbValuesError extends CmsServiceError {

    public CmsGetUrcbValuesError() {
        super(new InnerGetURCBValuesErrorPDU());
    }

    public CmsGetUrcbValuesError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetUrcbValuesError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
