package com.ysh.jcms.core.pdu.directory;

import com.ysh.jcms.data.InnerGetAllDataValuesErrorPDU;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * GetAllDataValues-ErrorPDU ::= ServiceError — 8.3.4
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsGetAllDataValuesError extends CmsServiceError {

    public CmsGetAllDataValuesError() {
        super(new InnerGetAllDataValuesErrorPDU());
    }

    public CmsGetAllDataValuesError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetAllDataValuesError value(int v) {
        super.value(v);
        return this;
    }

}
