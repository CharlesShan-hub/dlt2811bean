package com.ysh.jcms.pdu.log;

import com.ysh.jcms.data.InnerGetLCBValuesErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * GetLCBValues-ErrorPDU ::= ServiceError — 8.8.2
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsGetLcbValuesError extends CmsServiceError {

    public CmsGetLcbValuesError() {
        super(new InnerGetLCBValuesErrorPDU());
    }

    public CmsGetLcbValuesError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetLcbValuesError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
