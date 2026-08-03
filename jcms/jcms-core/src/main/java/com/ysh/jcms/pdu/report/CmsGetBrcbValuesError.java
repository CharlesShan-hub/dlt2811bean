package com.ysh.jcms.pdu.report;

import com.ysh.jcms.data.InnerGetBRCBValuesErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * GetBRCBValues-ErrorPDU ::= ServiceError — 8.7.2
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsGetBrcbValuesError extends CmsServiceError {

    public CmsGetBrcbValuesError() {
        super(new InnerGetBRCBValuesErrorPDU());
    }

    public CmsGetBrcbValuesError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetBrcbValuesError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
