package com.ysh.jcms.pdu.dataset;

import com.ysh.jcms.data.InnerDeleteDataSetErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * DeleteDataSet-ErrorPDU ::= ServiceError — 8.5.4
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsDeleteDataSetError extends CmsServiceError {

    public CmsDeleteDataSetError() {
        super(new InnerDeleteDataSetErrorPDU());
    }

    public CmsDeleteDataSetError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsDeleteDataSetError value(int v) {
        super.value(v);
        return this;
    }
}
