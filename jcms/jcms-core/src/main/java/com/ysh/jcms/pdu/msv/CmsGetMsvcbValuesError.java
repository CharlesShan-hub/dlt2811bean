package com.ysh.jcms.pdu.msv;

import com.ysh.jcms.data.InnerGetMSVCBValuesErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * GetMSVCBValues-ErrorPDU ::= ServiceError — 8.10.2
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsGetMsvcbValuesError extends CmsServiceError {

    public CmsGetMsvcbValuesError() {
        super(new InnerGetMSVCBValuesErrorPDU());
    }

    public CmsGetMsvcbValuesError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetMsvcbValuesError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
