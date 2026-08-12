package com.ysh.jcms.core.pdu.directory;

import com.ysh.jcms.data.InnerGetAllCBValuesErrorPDU;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * GetAllCBValues-ErrorPDU ::= ServiceError — 8.3.6
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsGetAllCbValuesError extends CmsServiceError {

    public CmsGetAllCbValuesError() {
        super(new InnerGetAllCBValuesErrorPDU());
    }

    public CmsGetAllCbValuesError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetAllCbValuesError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
