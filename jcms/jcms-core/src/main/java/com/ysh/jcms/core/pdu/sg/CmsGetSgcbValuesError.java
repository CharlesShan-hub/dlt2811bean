package com.ysh.jcms.core.pdu.sg;

import com.ysh.jcms.data.InnerGetSGCBValuesErrorPDU;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * GetSGCBValues-ErrorPDU ::= ServiceError — 8.6.6
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsGetSgcbValuesError extends CmsServiceError {

    public CmsGetSgcbValuesError() {
        super(new InnerGetSGCBValuesErrorPDU());
    }

    public CmsGetSgcbValuesError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetSgcbValuesError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
