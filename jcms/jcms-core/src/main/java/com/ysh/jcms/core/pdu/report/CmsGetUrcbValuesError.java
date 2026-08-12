package com.ysh.jcms.core.pdu.report;

import com.ysh.jcms.data.InnerGetURCBValuesErrorPDU;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * GetURCBValues-ErrorPDU ::= ServiceError — 8.7.4
 * }
 * </pre>
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
