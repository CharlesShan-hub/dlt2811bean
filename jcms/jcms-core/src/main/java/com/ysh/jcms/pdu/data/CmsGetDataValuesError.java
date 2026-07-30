package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.InnerGetDataValuesErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * GetDataValues-ErrorPDU ::= ServiceError — 8.4.1
 *
 * <p>Type alias, not a SEQUENCE.
 */
public class CmsGetDataValuesError extends CmsServiceError {

    public CmsGetDataValuesError() {
        super(new InnerGetDataValuesErrorPDU());
    }

    public CmsGetDataValuesError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetDataValuesError value(int v) {
        super.value(v);
        return this;
    }
}
