package com.ysh.jcms.core.pdu.log;

import com.ysh.jcms.data.InnerGetLCBValuesErrorPDU;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * GetLCBValues-ErrorPDU ::= ServiceError — 8.8.2
 * }
 * </pre>
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
