package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.InnerGetAllDataValuesErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * GetAllDataValues-ErrorPDU ::= ServiceError — 8.3.4
 *
 * <p>Type alias, not a SEQUENCE.
 */
public class CmsGetAllDataValuesError extends CmsServiceError {

    public CmsGetAllDataValuesError() {
        super(new InnerGetAllDataValuesErrorPDU());
    }

    @Override
    public CmsGetAllDataValuesError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
