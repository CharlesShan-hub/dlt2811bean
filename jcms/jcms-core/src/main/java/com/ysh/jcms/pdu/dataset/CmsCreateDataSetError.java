package com.ysh.jcms.pdu.dataset;

import com.ysh.jcms.data.InnerCreateDataSetErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * CreateDataSet-ErrorPDU ::= ServiceError — 8.5.3
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsCreateDataSetError extends CmsServiceError {

    public CmsCreateDataSetError() {
        super(new InnerCreateDataSetErrorPDU());
    }

    public CmsCreateDataSetError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsCreateDataSetError value(int v) {
        super.value(v);
        return this;
    }
}
