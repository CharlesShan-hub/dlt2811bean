package com.ysh.jcms.pdu.dataset;

import com.ysh.jcms.data.InnerGetDataSetDirectoryErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * GetDataSetDirectory-ErrorPDU ::= ServiceError — 8.5.5
 *
 * <p>Type alias, not a SEQUENCE.
 */
public class CmsGetDataSetDirectoryError extends CmsServiceError {

    public CmsGetDataSetDirectoryError() {
        super(new InnerGetDataSetDirectoryErrorPDU());
    }

    public CmsGetDataSetDirectoryError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetDataSetDirectoryError value(int v) {
        super.value(v);
        return this;
    }
}
