package com.ysh.jcms.core.pdu.dataset;

import com.ysh.jcms.data.InnerGetDataSetValuesErrorPDU;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * GetDataSetValues-ErrorPDU ::= ServiceError — 8.5.1
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsGetDataSetValuesError extends CmsServiceError {

    public CmsGetDataSetValuesError() {
        super(new InnerGetDataSetValuesErrorPDU());
    }

    public CmsGetDataSetValuesError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetDataSetValuesError value(int v) {
        super.value(v);
        return this;
    }
}
