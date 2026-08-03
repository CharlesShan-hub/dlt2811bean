package com.ysh.jcms.pdu.log;

import com.ysh.jcms.data.InnerGetLogStatusValuesErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * GetLogStatusValues-ErrorPDU ::= ServiceError — 8.8.6
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsGetLogStatusValuesError extends CmsServiceError {

    public CmsGetLogStatusValuesError() {
        super(new InnerGetLogStatusValuesErrorPDU());
    }

    public CmsGetLogStatusValuesError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetLogStatusValuesError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
